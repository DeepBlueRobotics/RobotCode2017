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

	private final Compressor compressor = RobotMap.drivetrainCompressor;
	private final DoubleSolenoid shiftPiston = RobotMap.drivetrainShiftPiston;

	private final AHRS gyro = RobotMap.ahrs;

	private final PowerDistributionPanel pdp = RobotMap.pdp;

	private PID velocityPID = new PID("DriveVelocity");
	private PID angularVelocityPID = new PID("DriveAngularVelocity");

	// Variables for motion profiling and acceleration control
	private double prevEncoderRate = 0, prevGyroRate = 0, prevTime = 0, driveLimit = 0, turnLimit = 0;
	private double gyroCalibrationInitalValue = 0, gyroDriftRate = 0;
	private Timer gyroDriftTimer = new Timer();

	public boolean isInArcadeDrive = true;
	private double currentSpeed = 0; // only used and changed in Arcade Drive
	private double currentTurn = 0;

	public PID distancePID = new PID("DriveDistance");
	public PID anglePID = new PID("DriveAngle");

	/**
	 * This method initializes the command used in teleop
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(Robot.drivetrain));
	}

	/**
	 * Allows toggling between arcade and tank teleop driving
	 */
	public void drive() {
		if (isInArcadeDrive) {
			currentSpeed = -Robot.oi.rightJoy.getY();
			currentTurn = -Robot.oi.leftJoy.getX();
			arcadeDrive(currentTurn, currentSpeed);
		} else {
			robotDrive.tankDrive(Robot.oi.leftJoy.getY(), -Robot.oi.rightJoy.getY());
		}
	}

	public void arcadeDrive(double speed, double turn) {
		robotDrive.arcadeDrive(speed, turn);
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
	 * Gets the distancePID object
	 * 
	 * @return the drivePID object
	 */
	public PID getDrivePID() {
		return distancePID;
	}

	/**
	 * Gets the anglePID object
	 * 
	 * @return the turnPID object
	 */
	public PID getTurnPID() {
		return anglePID;
	}
	
	/**
	 * Gets the velocityPID object
	 * 
	 * @return the velocityPID object
	 */
	public PID getVelocityPID() {
		return velocityPID;
	}
	
	/**
	 * Gets the angularVelocityPID object
	 * 
	 * @return the angularVelocityPID object
	 */
	public PID getAngularVelocityPID() {
		return angularVelocityPID;
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
	 * @return the distance that the robot moved relative to the encoders' last
	 *         reset
	 */
	public double getDistance() {
		return (leftEncoder.get() + rightEncoder.get()) / 2;
	}
	
	/**
	 * @return the angle that the robot turned relative to the gyro's last reset
	 */
	public double getAngle() {
		return gyro.getAngle();
	}

	/**
	 * @return the average speed of the two sides of the robot at the current
	 *         time
	 */
	public double getSpeed() {
		return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
	}

	/**
	 * @return the angular velocity
	 */
	public double getAngularVelocity() {
		return gyro.getRate();
	}

	/**
	 * Checks to see if the distance PID has reached the target
	 * 
	 * @return Whether distance target has been reached
	 */
	public boolean distanceReachedTarget() {
		return distancePID.reachedTarget();
	}

	/**
	 * Checks to see if the angle PID has reached the target
	 * 
	 * @return Whether angle target has been reached
	 */
	public boolean angleReachedTarget() {
		return anglePID.reachedTarget();
	}
	
	/**
	 * Checks to see if the speed PID has reached the target
	 * 
	 * @return Whether speed target has been reached
	 */
	public boolean speedReachedTarget() {
		return velocityPID.reachedTarget();
	}
	
	/**
	 * Checks to see if the angular velocity PID has reached the target
	 *  
	 * @return Whether angular velocity PID has reached the target
	 */
	public boolean angularVelocityReachedTarget() {
		return angularVelocityPID.reachedTarget();
	}

	/** 
	 * Updates and tests distancePID
	 */
	public void updateDistance() {
		distancePID.update(getDistance());
		robotDrive.arcadeDrive(distancePID.getOutput(), 0);
	}
	
	/**
	 * Updates and tests anglePID
	 */
	public void updateAngle() {
		anglePID.update(getAngle());
		robotDrive.arcadeDrive(0, anglePID.getOutput());
	}
	
	/**
	 * Updates linear and angular velocity PIDs for motion profiling
	 */
	public void updateVelocity() {
		// TODO (Ana T.) See todo of setVelocityTarget method
		velocityPID.update(getDistance());
		angularVelocityPID.update(getAngle());
		robotDrive.arcadeDrive(velocityPID.getOutput(), angularVelocityPID.getOutput());
	}
	
	/**
	 * Sets the distance for PID target
	 * 
	 * @param targetDistance
	 *            - the target distance being set to PID
	 */
	public void setDistanceTarget(double targetDistance) {
		distancePID.update((leftEncoder.get() + rightEncoder.get()) / 2);
		distancePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
	}

	/**
	 * Sets the angle for PID target
	 * 
	 * @param targetAngle
	 *            - the target angle in being set to PID
	 */
	public void setAngleTarget(double targetAngle) {
		anglePID.update(getAngle());
		anglePID.setRelativeLocation(0);
		anglePID.setTarget(targetAngle);
	}
	
	/**
	 * Sets targets for tracking velocity of robot for motion profiling
	 * 
	 * @param linVelTarget
	 * @param angVelTarget
	 */
	public void setVelocityTarget(double linVelTarget, double angVelTarget) {
		velocityPID.update(getSpeed());
		velocityPID.setRelativeLocation(0);
		velocityPID.setTarget(linVelTarget);

		angularVelocityPID.update(getAngularVelocity());
		angularVelocityPID.setRelativeLocation(0);
		angularVelocityPID.setTarget(angVelTarget);
	}

	/**
	 * For autonomous period, drives to angle given and then to distance given.
	 */
	public void autoDrive() {
		distancePID.update(getDistance());
		anglePID.update(getAngle());
		if (!anglePID.reachedTarget()) {
			arcadeDrive(0, anglePID.getOutput());
		} else {
			arcadeDrive(distancePID.getOutput(), 0);
		}
	}

	/**
	 * Just stops the robot, setting its motors to zero. Usually called after a
	 * command finishes.
	 */
	public void stopDrive() {
		arcadeDrive(0, 0);
	}

	@Override
	public void displayData() {
		putNumber("Left Speed", leftEncoder.getRate());
		putNumber("Right Speed", rightEncoder.getRate());
		putNumber("Average Speed", getSpeed());

		putNumber("Left Distance", leftEncoder.get());
		putNumber("Right Distance", rightEncoder.get());
		putNumber("Average Distance", getDistance());
		
		putNumber("Acceleration", (getEncoderRate() - prevEncoderRate) / (Timer.getFPGATimestamp() - prevTime));
		putNumber("Angular acceleration", (getGyroRate() - prevGyroRate) / (Timer.getFPGATimestamp() - prevTime));

		putNumber("Angle", gyro.getAngle());
		putNumber("Turn Speed", gyro.getRate());

		putBoolean("High Gear", false);

		putBoolean("Gear has been lifted", false);
		prevEncoderRate = getEncoderRate();
		prevGyroRate = getGyroRate();
		prevTime = Timer.getFPGATimestamp();
	}

	/**
	 * Shifts gears to whatever state they are not in
	 */
	public void shiftGears() {
		if (shiftPiston.get().toString().equals("kForward")) {
			// shift to high gear
			shiftPiston.set(DoubleSolenoid.Value.kReverse);
		} else {
			// shift to low gear
			shiftPiston.set(DoubleSolenoid.Value.kForward);
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
		if (current >= Robot.getPref("drivetrainMaxCurrent", 110))
			return true;
		return false;
	}

	/**
	 * returns whether or not the AnalogInput detects an object blocking the
	 * light
	 */


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

		// Computes output velocity using PID
		double outputV = velocityPID.getOutput() + kV * v + kA * a;
		// Computes output angular velocity using PID
		double outputW = angularVelocityPID.getOutput() + kW * w + kAlpha * alpha;

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

	private double getGyroAngle() {
		return gyro.getAngle() - gyroDriftRate * gyroDriftTimer.get();

	}

	public double getEncoderDistance() {
		return (-leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	private double getGyroRate() {
		return gyro.getRate() - gyroDriftRate;
	}

	private double getEncoderRate() {
		return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
	}

}
