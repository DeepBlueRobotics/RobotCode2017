package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.PID;
import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem implements DashboardInterface {

	private final SpeedController leftMotor = RobotMap.drivetrainLeftMotor;
	private final SpeedController rightMotor = RobotMap.drivetrainRightMotor;
	private final RobotDrive robotDrive = RobotMap.drivetrainRobotDrive;
	private final Encoder leftEncoder = RobotMap.drivetrainLeftEncoder;
	private final Encoder rightEncoder = RobotMap.drivetrainRightEncoder;
	// private final AnalogGyro gyro = RobotMap.drivetrainGyro;
	private final DigitalInput gearLiftedSwitch = RobotMap.gearLiftedLimitSwitch;

	private final Compressor compressor = RobotMap.drivetrainCompressor;
	private final DoubleSolenoid leftShiftPiston = RobotMap.drivetrainLeftShiftPiston;
	private final DoubleSolenoid rightShiftPiston = RobotMap.drivetrainRightShiftPiston;

	private final AHRS gyro = RobotMap.ahrs;

	private final PowerDistributionPanel pdp = RobotMap.pdp;

	public boolean isInArcadeDrive = true;
	private double currentSpeed = 0; // only used and changed in Arcade Drive
	private double currentTurn = 0;

	public PID drivePID = new PID("drivePID");
	public PID turnPID = new PID("turnPID");

	/**
	 * This method initializes the command used in teleop
	 */
	public void initDefaultCommand() {

		setDefaultCommand(new TeleopDrive());

	}

	/**
	 * Resets the encoders to return zero at that point
	 */
	public void resetEncoder() {
		leftEncoder.reset();
		rightEncoder.reset();
	}

	/**
	 * Resets the gyro to return zero at that point
	 */
	public void resetGyro() {
		gyro.reset();
	}

	/**
	 * @return the angle that the robot turned relative to the gyro's last reset
	 */
	public double getAngle() {
		return gyro.getAngle();
	}

	/**
	 * @return the distance that the robot moved relative to the encoders' last
	 *         reset
	 */
	public double getDistance() {
		return (leftEncoder.get() + rightEncoder.get()) / 2;
	}

	/**
	 * @return the average speed of the two sides of the robot at the current
	 *         time
	 */
	public double getSpeed() {
		return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
	}

/**
	 * Checks to see if the distance PID has reached the target
	 * @return Whether distance target has been reached
	 */
	public boolean distanceReachedTarget() {
		return drivePID.reachedTarget();
	}
    
	/**
	 * Checks to see if the angle PID has reached the target
	 * @return Whether angle target has been reached
	 */
	public boolean angleReachedTarget() {
		return turnPID.reachedTarget();
	}
	/**
	 * Updates and tests anglePID
	 */
	public void updateAngle(){
		turnPID.update(getAngle());
		robotDrive.arcadeDrive(0, turnPID.getOutput());
	}

	/**
	 * Sets the distance for PID target
	 * @param targetDistance - the target distance being set to PID
	 */
	public void setDistanceTarget(double targetDistance){
		drivePID.update((leftEncoder.get() + rightEncoder.get()) / 2);
		drivePID.setRelativeLocation(0);
		drivePID.setTarget(targetDistance);
	}
	
	/**
	 * Sets the angle for PID target
	 * @param targetAngle - the target angle in being set to PID
	 */
	public void setAngleTarget(double targetAngle) {
		turnPID.update(getAngle());
		turnPID.setRelativeLocation(0);
		turnPID.setTarget(targetAngle);
	}
	
	/**
	 * Forces the robot's turn and move speed to change at a max of 5% each
	 * iteration
	 */
	public void gradualDrive() {
		if (isInArcadeDrive) {
			currentSpeed += Math.signum(Robot.oi.rightJoy.getY() - currentSpeed) * 0.05;
			currentTurn += Math.signum(Robot.oi.leftJoy.getX() - currentTurn) * 0.05;
			robotDrive.arcadeDrive(currentTurn, currentSpeed);
		} else {
			robotDrive.tankDrive(leftMotor.get() + Math.signum(Robot.oi.leftJoy.getY() - leftMotor.get()) * 0.05,
					rightMotor.get() + Math.signum(Robot.oi.rightJoy.getY() - rightMotor.get()) * 0.05);
		}
	}

	/**
	 * Allows toggling between arcade and tank teleop driving
	 */
	public void drive() {
		if (isInArcadeDrive) {
			currentSpeed = Robot.oi.rightJoy.getY();
			currentTurn = Robot.oi.leftJoy.getX();
			robotDrive.arcadeDrive(currentTurn, currentSpeed);
		} else {
			robotDrive.tankDrive(Robot.oi.leftJoy.getY(), Robot.oi.rightJoy.getY());
		}
	}

	/**
	 * For autonomous period, drives to angle given and then to distance given.
	 */
	public void autoDrive() {
		drivePID.update(getDistance());
		turnPID.update(getAngle());
		if (!turnPID.reachedTarget()) {
			robotDrive.arcadeDrive(0, turnPID.getOutput());
		} else {
			robotDrive.arcadeDrive(drivePID.getOutput(), 0);
		}
	}

	/**
	 * Just stops the robot, setting its motors to zero. Usually called after a
	 * command finishes.
	 */
	public void stopDrive() {
		robotDrive.arcadeDrive(0, 0);
	}

	@Override
	public void displayData() {
		SmartDashboard.putNumber("Left Speed", leftEncoder.getRate());
		SmartDashboard.putNumber("Right Speed", rightEncoder.getRate());
		SmartDashboard.putNumber("Average Speed", getSpeed());

		SmartDashboard.putNumber("Left Distance", leftEncoder.get());
		SmartDashboard.putNumber("Right Distance", rightEncoder.get());
		SmartDashboard.putNumber("Average Distance", getDistance());

		SmartDashboard.putNumber("Angle", gyro.getAngle());
		SmartDashboard.putNumber("Turn Speed", gyro.getRate());
		
		putBoolean("high gear", false);
	}

	/**
	 * Shifts gears to whatever state they are not in
	 * */
	public void shiftGears(){
		if(leftShiftPiston.get().toString().equals("kForward")){
			//shift to high gear
			leftShiftPiston.set(DoubleSolenoid.Value.kReverse);
			rightShiftPiston.set(DoubleSolenoid.Value.kReverse);
		}
		else{
			//shift to low gear
			leftShiftPiston.set(DoubleSolenoid.Value.kForward);
			rightShiftPiston.set(DoubleSolenoid.Value.kForward);
		}
	}

	/**
	 * Monitors current draw of drivetrain
	 * 
	 * @return whether the robot should shift to low gear
	 */
	public boolean currentControl() {
		int channel = (int) (Robot.getPref("drivetrain channel", 0));
		double current = pdp.getCurrent(channel);
		if (current >= 110)
			return true;
		return false;
	}
}
