package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
import org.usfirst.frc199.Robot2017.motion.PID;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem implements DrivetrainInterface {

	private final SpeedController leftMotor = RobotMap.drivetrainLeftMotor;
	private final SpeedController rightMotor = RobotMap.drivetrainRightMotor;
	private final RobotDrive robotDrive = RobotMap.drivetrainRobotDrive;
	private final Encoder leftEncoder = RobotMap.drivetrainLeftEncoder;
	private final Encoder rightEncoder = RobotMap.drivetrainRightEncoder;
	// private final AnalogGyro gyro = RobotMap.drivetrainGyro;
	private final DigitalInput gearLiftedSwitch = RobotMap.gearLiftedLimitSwitch;
	private final AnalogInput AI = RobotMap.driverAI;

	private final Compressor compressor = RobotMap.drivetrainCompressor;
	private final DoubleSolenoid leftShiftPiston = RobotMap.drivetrainLeftShiftPiston;
	private final DoubleSolenoid rightShiftPiston = RobotMap.drivetrainRightShiftPiston;

	private final AHRS gyro = RobotMap.ahrs;

	private final PowerDistributionPanel pdp = RobotMap.pdp;

	private PID distancePID = new PID("DriveDistance");
	private PID anglePID = new PID("DriveAngle");
	private PID velocityPID = new PID("DriveVelocity");
	private PID angularVelocityPID = new PID("DriveAngularVelocity");

	// Variables for motion profiling and acceleration control
	private double prevEncoderRate = 0, prevGyroRate = 0, prevTime = 0, driveLimit = 0, turnLimit = 0;
	private double gyroCalibrationInitalValue = 0, gyroDriftRate = 0;
	private Timer gyroDriftTimer = new Timer();

	public boolean isInArcadeDrive = true;
	private double currentSpeed = 0; // only used and changed in Arcade Drive
	private double currentTurn = 0;

	public PID drivePID = new PID("drivePID");
	public PID turnPID = new PID("turnPID");

	/**
	 * This method initializes the command used in teleop
	 */
	public void initDefaultCommand() {

		setDefaultCommand(new TeleopDrive(Robot.drivetrain));

	}

	/**
	 * Gets the drivePID object
	 * 
	 * @return the drivePID object
	 */
	public PID getDrivePID() {
		return drivePID;
	}

	/**
	 * Gets the turnPID object
	 * 
	 * @return the turnPID object
	 */
	public PID getTurnPID() {
		return turnPID;
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
	 * 
	 * @return Whether distance target has been reached
	 */
	public boolean distanceReachedTarget() {
		return drivePID.reachedTarget();
	}

	/**
	 * Checks to see if the angle PID has reached the target
	 * 
	 * @return Whether angle target has been reached
	 */
	public boolean angleReachedTarget() {
		return turnPID.reachedTarget();
	}

	/**
	 * Updates and tests anglePID
	 */
	public void updateAngle() {
		turnPID.update(getAngle());
		robotDrive.arcadeDrive(0, turnPID.getOutput());
	}

	/**
	 * Sets the distance for PID target
	 * 
	 * @param targetDistance
	 *            - the target distance being set to PID
	 */
	public void setDistanceTarget(double targetDistance) {
		drivePID.update((leftEncoder.get() + rightEncoder.get()) / 2);
		drivePID.setRelativeLocation(0);
		drivePID.setTarget(targetDistance);
	}

	/**
	 * Sets the angle for PID target
	 * 
	 * @param targetAngle
	 *            - the target angle in being set to PID
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

		SmartDashboard.putBoolean("high gear", false);

		SmartDashboard.putBoolean("gear has been lifted", false);
	}

	/**
	 * Shifts gears to whatever state they are not in
	 */
	public void shiftGears() {
		if (leftShiftPiston.get().toString().equals("kForward")) {
			// shift to high gear
			leftShiftPiston.set(DoubleSolenoid.Value.kReverse);
			rightShiftPiston.set(DoubleSolenoid.Value.kReverse);
		} else {
			// shift to low gear
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

	/**
	 * Sets targets for tracking velocity of robot for motion profiling
	 * 
	 * @param linVelTarget
	 * @param angVelTarget
	 */
	public void setVelocityTarget(double linVelTarget, double angVelTarget) {
		// TODO (Ana T.) Write method for setting target of velocity PID. Check
		// last
		// year's code cause that looked more complicated than it needs to be.
		// Figure out why
	}

	/**
	 * returns whether or not the AnalogInput detects an object blocking the
	 * light
	 */
	public boolean gearLifted() {
		// return if gear lifted or not
		if (AI.getVoltage() > 0.19) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Updates linear and angular velocity PIDs for motion profiling
	 */
	public void updateVelocity() {
		// TODO (Ana T.) See todo of setVelocityTarget method

	}

	/**
	 * Uses PID to reach target velocity
	 * 
	 * @param v
	 *            - linear velocity in inches/second
	 * @param w
	 *            - angular velocity in degrees/second
	 * @param a
	 *            - acceleration in inches/second/second
	 * @param alpha
	 *            - angular acceleration in degrees/second/second
	 */
	public void followTrajectory(double v, double w, double a, double alpha) {
		velocityPID.setTarget(v);
		angularVelocityPID.setTarget(w);
		velocityPID.update(getEncoderRate());
		angularVelocityPID.update(getGyroRate());

		double kV = 1.0 / Robot.getPref("DriveMaxV", .01);
		double kW = 1.0 / Robot.getPref("DriveMaxW", .01);
		double kA = SmartDashboard.getNumber("MotionProfile/kA", Robot.getPref("DrivekA", 0.0));
		double kAlpha = SmartDashboard.getNumber("MotionProfile/kAlpha", Robot.getPref("DrivekAlpha", 0.0));

		double outputV = velocityPID.getOutput() + kV * v + kA * a; // Computes
																	// Output
																	// Velocity
																	// Using PID
		double outputW = angularVelocityPID.getOutput() + kW * w + kAlpha * alpha; // Computes
																					// Output
																					// Angular
																					// Velocity
																					// Using
																					// PID

		arcadeDrive(outputV, outputW);

		SmartDashboard.putNumber("MotionProfile/L", getEncoderDistance());
		SmartDashboard.putNumber("MotionProfile/V", getEncoderRate());
		SmartDashboard.putNumber("MotionProfile/A",
				(getEncoderRate() - prevEncoderRate) / (Timer.getFPGATimestamp() - prevTime));
		SmartDashboard.putNumber("MotionProfile/Theta", getGyroAngle());
		SmartDashboard.putNumber("MotionProfile/W", getGyroRate());
		SmartDashboard.putNumber("MotionProfile/Alpha",
				(getGyroRate() - prevGyroRate) / (Timer.getFPGATimestamp() - prevTime));
		prevEncoderRate = getEncoderRate();
		prevGyroRate = getGyroRate();
		prevTime = Timer.getFPGATimestamp();
	}

	public void arcadeDrive(double speed, double turn) {
		// TODO Auto-generated method stub
		robotDrive.arcadeDrive(speed, -turn);
	}

	private double getGyroAngle() {
		// TODO Auto-generated method stub
		return gyro.getAngle() - gyroDriftRate * gyroDriftTimer.get();

	}

	public double getEncoderDistance() {
		// TODO Auto-generated method stub
		return (-leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	private double getGyroRate() {
		// TODO Auto-generated method stub
		return gyro.getRate() - gyroDriftRate;
	}

	private double getEncoderRate() {
		// TODO Auto-generated method stub
		return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
	}
}
