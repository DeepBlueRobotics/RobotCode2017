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
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends Subsystem implements DrivetrainInterface {

	private final VictorSP leftMotor = (VictorSP) RobotMap.drivetrainLeftMotor;
	private final VictorSP rightMotor = (VictorSP) RobotMap.drivetrainRightMotor;
	private final RobotDrive robotDrive = RobotMap.drivetrainRobotDrive;
	private final Encoder leftEncoder = RobotMap.drivetrainLeftEncoder;
	private final Encoder rightEncoder = RobotMap.drivetrainRightEncoder;

	private final Compressor compressor = RobotMap.drivetrainCompressor;
	private final DoubleSolenoid shiftPiston = RobotMap.drivetrainShiftPiston;

	private final AHRS gyro = RobotMap.ahrs;

	private final PowerDistributionPanel pdp = RobotMap.pdp;

	private final AnalogInput leftUSsensor = RobotMap.drivetrainLeftUSsensor;
	private final AnalogInput rightUSsensor = RobotMap.drivetrainRightUSsensor;

	private PID velocityPID = new PID("DriveVelocity");
	private PID angularVelocityPID = new PID("DriveAngularVelocity");

	// Variables for motion profiling and acceleration control
	private double prevEncoderRate = 0, prevGyroRate = 0, prevTime = 0, driveLimit = 0, turnLimit = 0, prevLeftEnc = 0,
			prevRightEnc = 0;
	private double gyroCalibrationInitalValue = 0, gyroDriftRate = 0;
	private Timer gyroDriftTimer = new Timer();

	private double currentSpeed = 0; // only used and changed in Arcade Drive
	private double currentTurn = 0;

	// for ultrasonic sensors
	private final double distBtwnUSsensors = 9.9;
	private final double distFromUSToRobotFront = 7.5;
	private final double targetUSDist = 3.5;

	private DriveTypes currentDrive = DriveTypes.ARCADE;

	private PID distancePID = new PID("DriveDistance");
	private PID anglePID = new PID("DriveAngle");
	private PID leftDriveVelocityPID = new PID("LeftDriveVelocity");
	private PID rightDriveVelocityPID = new PID("RightDriveVelocity");
	public boolean shiftedHigh = true;

	public Drivetrain() {
		super();
		putString("~TYPE~", "Drivetrain");
	}

	/**
	 * This method initializes the command used in teleop
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopDrive(Robot.drivetrain));
	}

	/**
	 * Changes the drive type
	 */
	public void toggleDriveType() {
		if (currentDrive == DriveTypes.ARCADE) {
			currentDrive = DriveTypes.TANK;
		} else if (currentDrive == DriveTypes.TANK) {
			currentDrive = DriveTypes.DRIFT_TANK;
			rightDriveVelocityPID.setTarget(0, true);
			leftDriveVelocityPID.setTarget(0, true);
		} else if (currentDrive == DriveTypes.DRIFT_TANK) {
			currentDrive = DriveTypes.DRIFT_ARCADE;
			rightDriveVelocityPID.setTarget(0, true);
			leftDriveVelocityPID.setTarget(0, true);
		} else if (currentDrive == DriveTypes.DRIFT_ARCADE) {
			currentDrive = DriveTypes.ARCADE;
			rightDriveVelocityPID.setTarget(0, true);
			leftDriveVelocityPID.setTarget(0, true);
		}
	}

	/**
	 * 
	 * @return either DriveTypes.ARCADE, DriveTypes.TANK or
	 *         DriveTypes.DRIFT_TANK
	 */
	public DriveTypes getDriveType() {
		return currentDrive;
	}

	/**
	 * Allows toggling between arcade and tank teleop driving
	 */
	public void drive() {
		currentSpeed = Robot.oi.rightJoy.getY();
		currentTurn = Robot.oi.leftJoy.getX();
		if (currentDrive == DriveTypes.ARCADE) {
			arcadeDrive(currentTurn, currentSpeed);
		} else if (currentDrive == DriveTypes.TANK) {
			robotDrive.tankDrive(-Robot.oi.leftJoy.getY(), Robot.oi.rightJoy.getY());
		} else if (currentDrive == DriveTypes.DRIFT_TANK) {
			double leftValue = -Robot.oi.leftJoy.getY();
			if (Math.abs(leftValue) < Robot.getPref("Tank deadband", .1)) {
				leftValue = 0;
				leftDriveVelocityPID.setTarget(0, true);
			}
			double rightValue = -Robot.oi.rightJoy.getY();
			if (Math.abs(rightValue) < Robot.getPref("Tank deadband", .1)) {
				rightValue = 0;
				rightDriveVelocityPID.setTarget(0, true);
			}
			unevenTankDrive(leftValue, rightValue);
		} else if (currentDrive == DriveTypes.DRIFT_ARCADE) {
			double turnValue = Robot.oi.leftJoy.getX();
			double speedValue = -Robot.oi.rightJoy.getY();
			speedValue = speedValue*Math.abs(speedValue);
			turnValue = turnValue*Math.abs(turnValue);

			if (Math.abs(speedValue) < Robot.getPref("Arcade deadband", .1)) {
				speedValue = 0;
			}
			if (Math.abs(turnValue) < Robot.getPref("Arcade deadband", .1)) {
				turnValue = 0;
			}
			if (Math.abs(turnValue) < Robot.getPref("Arcade deadband", .05)
					&& Math.abs(speedValue) < Robot.getPref("Arcade deadband", .05)) {
				leftDriveVelocityPID.setTarget(0, true);
				rightDriveVelocityPID.setTarget(0, true);
			}
			unevenArcadeDrive(speedValue, turnValue);
		}
	}

	public void arcadeDrive(double speed, double turn) {
		robotDrive.arcadeDrive(speed, turn);
	}

	/**
	 * Accounts for drift when in arcade drive Sets each motor's respective
	 * target speed based on speed to joystick ratios
	 */
	public void unevenArcadeDrive(double speedJoy, double turnJoy) {
		double ratio = Robot.getPref("lowGearSpeedRatio", 68);
		if(shiftedHigh) ratio = Robot.getPref("highGearSpeedRatio", 175);
		setRightSpeedTarget(ratio*(speedJoy - turnJoy));
		setLeftSpeedTarget(ratio*(speedJoy + turnJoy));
		updateBothSpeedPID();
	}

	/**
	 * Accounts for drift when in tank drive Sets each motor's respective target
	 * speed based on speed to joystick ratios
	 */
	public void unevenTankDrive(double leftJoy, double rightJoy) {
		double ratio = Robot.getPref("lowGearSpeedRatio", 68);
		if(shiftedHigh) ratio = Robot.getPref("highGearSpeedRatio", 175);
		setRightSpeedTarget(ratio*rightJoy);
		setLeftSpeedTarget(ratio*leftJoy);
		updateBothSpeedPID();
	}

	public boolean inHighGear() {
		return shiftPiston.get().toString().equals("kReverse");
	}

	/**
	 * Forces the robot's turn and move speed to change at a max of 5% each
	 * iteration
	 */
	public void gradualDrive() {
		if (currentDrive == DriveTypes.ARCADE) {
			currentSpeed += Math.signum(Robot.oi.rightJoy.getY() - currentSpeed) * 0.05;
			currentTurn += Math.signum(Robot.oi.leftJoy.getX() - currentTurn) * 0.05;
			robotDrive.arcadeDrive(currentTurn, currentSpeed);
		} else {
			robotDrive.tankDrive(leftMotor.get() + Math.signum(Robot.oi.leftJoy.getY() - leftMotor.get()) * 0.05,
					rightMotor.get() + Math.signum(Robot.oi.rightJoy.getY() - rightMotor.get()) * 0.05);
		}
	}

	/**
	 * For autonomous period, drives to angle given and then to distance given.
	 */
	public void autoDrive() {
		 if (!angleReachedTarget()) {
			 updateAnglePID();
		 } else {
			 updateDistancePID();
		 }
	}

	/**
	 * Just stops the robot, setting its motors to zero. Usually called after a
	 * command finishes.
	 */
	public void stopDrive() {
		unevenArcadeDrive(0, 0);
		arcadeDrive(0, 0);
	}

	/**
	 * @return the distance that the robot moved relative to the encoders' last
	 *         reset
	 */
	public double getDistance() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	/**
	 * Gets the distancePID Only used in tests
	 * 
	 * @return the distancePID
	 */
	public PID getDistancePID() {
		return distancePID;
	}

	/**
	 * Sets the distance for PID target
	 * 
	 * @param targetDistance - the target distance being set to PID
	 */
	public void setDistanceTarget(double targetDistance) {
		distancePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
		distancePID.update(getDistance());
	}

	/**
	 * Updates and tests/runs distancePID
	 */
	public void updateDistancePID() {
		distancePID.update(getDistance());
		unevenArcadeDrive(distancePID.getOutput(), 0);
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
	 * @return the angle that the robot turned relative to the gyro's last reset
	 */
	public double getAngle() {
		return gyro.getAngle() - gyroDriftRate * gyroDriftTimer.get();
	}

	/**
	 * Gets the anglePID Only used in tests
	 * 
	 * @return the anglePID
	 */
	public PID getAnglePID() {
		return anglePID;
	}

	/**
	 * Sets the angle for PID target
	 * 
	 * @param targetAngle - the target angle in being set to PID
	 */
	public void setAngleTarget(double targetAngle) {
		anglePID.setRelativeLocation(0);
		anglePID.setTarget(targetAngle);
		anglePID.update(getAngle());
	}

	/**
	 * Updates and tests/runs anglePID
	 */
	public void updateAnglePID() {
		anglePID.update(getAngle());
		unevenArcadeDrive(0, anglePID.getOutput());
	}

	/**
	 * Checks to see if the angle PID has reached the target
	 * 
	 * @return Whether angle target has been reached
	 */
	public boolean angleReachedTarget() {
		return anglePID.reachedTarget();
	}

	// NEW PIDS START HERE!!!

	/**
	 * @return the speed of the left side of the robot at the current time
	 */
	public double getLeftSpeed() {
		return leftEncoder.getRate();

	}

	/**
	 * Gets the leftSpeedPID Only used in tests
	 * 
	 * @return the leftSpeedPID
	 */
	public PID getLeftSpeedPID() {
		return leftDriveVelocityPID;
	}

	/**
	 * Sets the left speed for PID target
	 * 
	 * @param targetSpeed - the target left speed being set to PID
	 */
	public void setLeftSpeedTarget(double targetSpeed) {
		leftDriveVelocityPID.setTarget(targetSpeed, false);
		leftDriveVelocityPID.update(leftEncoder.getRate());
	}

	/**
	 * Updates and tests/runs leftDriveSpeedPID
	 */
	public void updateLeftSpeedPID() {
		leftDriveVelocityPID.update(leftEncoder.getRate());
		leftMotor.set(leftDriveVelocityPID.getOutput());
	}
	
	public void updateBothSpeedPID() { 

		leftDriveVelocityPID.update(leftEncoder.getRate());

		rightDriveVelocityPID.update(rightEncoder.getRate());

		robotDrive.tankDrive(leftDriveVelocityPID.getOutput(), -rightDriveVelocityPID.getOutput());
	}

	/**
	 * Checks to see if the left speed PID has reached the target
	 * 
	 * @return Whether left speed target has been reached
	 */
	public boolean leftSpeedReachedTarget() {
		return leftDriveVelocityPID.reachedTarget();
	}

	// STARTS RIGHT HALF

	/**
	 * @return the speed of the right side of the robot at the current time
	 */
	public double getRightSpeed() {
		return rightEncoder.getRate();

	}

	/**
	 * Gets the rightSpeedPID Only used in tests
	 * 
	 * @return the rightSpeedPID
	 */
	public PID getRightSpeedPID() {
		return rightDriveVelocityPID;
	}

	/**
	 * Sets the right speed for PID target
	 * 
	 * @param targetSpeed - the target right speed being set to PID
	 */
	public void setRightSpeedTarget(double targetSpeed) {
		rightDriveVelocityPID.setTarget(targetSpeed, false);
		rightDriveVelocityPID.update(rightEncoder.getRate());
	}

	/**
	 * Updates and tests/runs rightDriveSpeedPID
	 */
	public void updateRightSpeedPID() {
		rightDriveVelocityPID.update(rightEncoder.getRate());
		rightMotor.set(rightDriveVelocityPID.getOutput());
	}

	/**
	 * Checks to see if the right speed PID has reached the target
	 * 
	 * @return Whether right speed target has been reached
	 */
	public boolean rightSpeedReachedTarget() {
		return rightDriveVelocityPID.reachedTarget();
	}

	// NEW PIDS END HERE!

	/**
	 * @return the average speed of the two sides of the robot at the current
	 *         time
	 */
	public double getVelocity() {
		return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
	}

	/**
	 * Sets targets for tracking velocity of robot for motion profiling
	 * 
	 * @param linVelTarget
	 * @param angVelTarget
	 */
	public void setVelocityTarget(double linVelTarget, double angVelTarget) {
		velocityPID.setRelativeLocation(0);
		velocityPID.setTarget(linVelTarget);
		velocityPID.update(getVelocity());

		angularVelocityPID.setRelativeLocation(0);
		angularVelocityPID.setTarget(angVelTarget);
		angularVelocityPID.update(getAngularVelocity());
	}

	/**
	 * Updates and tests/runs linear and angular velocity PIDs for motion
	 * profiling
	 */
	public void updateVelocityPIDs() {
		velocityPID.update(getVelocity());
		angularVelocityPID.update(getAngularVelocity());
		robotDrive.arcadeDrive(angularVelocityPID.getOutput(), -velocityPID.getOutput());
	}

	/**
	 * Checks to see if the speed PID has reached the target
	 * 
	 * @return Whether speed target has been reached
	 */
	public boolean velocityReachedTarget() {
		return velocityPID.reachedTarget();
	}

	/**
	 * Gets the angular acceleration
	 * 
	 * @return the angular acceleration
	 */
	public double getAngularAcceleration() {
		prevGyroRate = getAngularVelocity();
		prevTime = Timer.getFPGATimestamp();
		return (getAngularVelocity() - prevGyroRate) / (Timer.getFPGATimestamp() - prevTime);
	}

	/**
	 * @return the angular velocity
	 */
	public double getAngularVelocity() {
		return gyro.getRate();
	}

	/**
	 * Checks to see if the angular velocity PID has reached the target
	 * 
	 * @return Whether angular velocity PID has reached the target
	 */
	public boolean angularVelocityPIDReachedTarget() {
		return angularVelocityPID.reachedTarget();
	}

	/**
	 * Uses PID to reach target velocity
	 * 
	 * @param v - linear velocity in inches/second
	 * @param w - angular velocity in degrees/second
	 * @param a - acceleration in inches/second/second
	 * @param alpha - angular acceleration in degrees/second/second
	 */
	public void followTrajectory(double v, double w, double a, double alpha) {
		velocityPID.setTarget(v);
		angularVelocityPID.setTarget(w);
		velocityPID.update(getVelocity());
		angularVelocityPID.update(getAngularVelocity());

		double kV = 1.0 / Robot.getPref("DriveMaxV", .01);
		double kW = 1.0 / Robot.getPref("DriveMaxW", .01);
		double kA = SmartDashboard.getNumber("MotionProfile/kA", Robot.getPref("DrivekA", 0.0));
		double kAlpha = SmartDashboard.getNumber("MotionProfile/kAlpha", Robot.getPref("DrivekAlpha", 0.0));

		// Computes output velocity using PID
		double outputV = velocityPID.getOutput() + kV * v + kA * a;
		// Computes output angular velocity using PID
		double outputW = angularVelocityPID.getOutput() + kW * w + kAlpha * alpha;

		arcadeDrive(outputV, outputW);

		SmartDashboard.putNumber("MotionProfile/L", getDistance());
		SmartDashboard.putNumber("MotionProfile/V", getVelocity());
		SmartDashboard.putNumber("MotionProfile/A",
				(getVelocity() - prevEncoderRate) / (Timer.getFPGATimestamp() - prevTime));
		SmartDashboard.putNumber("MotionProfile/Theta", getAngle());
		SmartDashboard.putNumber("MotionProfile/W", getAngularVelocity());
		SmartDashboard.putNumber("MotionProfile/Alpha",
				(getAngularVelocity() - prevGyroRate) / (Timer.getFPGATimestamp() - prevTime));
		prevEncoderRate = getVelocity();
		prevGyroRate = getAngularVelocity();
		prevTime = Timer.getFPGATimestamp();
	}

	/**
	 * Converts volts from ultrasonic sensors into inches based on equation
	 * derived from testing.
	 * 
	 * @return distance in inches
	 */
	public double convertVoltsToInches(double volts) {
		return (volts + 4.38e-3) / 0.012;
	}

	/**
	 * Gets the voltage from one of two ultrasonic sensors.
	 * 
	 * @param side tells which ultrasonic sensor to pull data from (left or
	 *            right) if side true, gets left voltage if side false, gets
	 *            right voltage
	 */
	public double getUSVoltage(boolean side) {
		if (side) {
			return leftUSsensor.getVoltage();
		} else {
			return rightUSsensor.getVoltage();
		}
	}

	/**
	 * Calculates the distance the robot needs to drive to reach target distance
	 * from wall based on the average of the current US sensor readings. Uses
	 * the average of the readings because this is measured before the robot
	 * auto truns but is used to execute auto drive after the robot auto turns.
	 * The average distance from the wall will not change in and ideal scenario
	 * because the distance from the robot center will not change. Henceforth,
	 * average is used.
	 * 
	 * @return the distance to be antered into a drive PID
	 */
	public double getUSDistToDrive() {
		double leftDist = convertVoltsToInches(getUSVoltage(true));
		double rightDist = convertVoltsToInches(getUSVoltage(false));
		return ((leftDist + rightDist) / 2 - distFromUSToRobotFront) - targetUSDist;
	}

	/**
	 * Calculates the angle needed to turn to to approach the wall head on.
	 * 
	 * @return the angle to turn to
	 */
	public double getUSTargetAngle() {
		double leftDist = convertVoltsToInches(getUSVoltage(true));
		double rightDist = convertVoltsToInches(getUSVoltage(false));
		return Math.toDegrees(Math.atan((leftDist - rightDist) / (distBtwnUSsensors)));
	}

	/**
	 * Shifts gears to whatever state they are not in
	 */
	public void shiftGears() {
		if (!shiftedHigh) {
			// shift to high gear
			shiftPiston.set(DoubleSolenoid.Value.kReverse);
			shiftedHigh = true;
		} else {
			// shift to low gear
			shiftPiston.set(DoubleSolenoid.Value.kForward);
			shiftedHigh = false;
		}
	}

	/**
	 * Sets the shifter piston to neutral
	 */
	public void setShifterNeutral() {
		shiftPiston.set(DoubleSolenoid.Value.kOff);
	}

	/**
	 * Monitors current draw of drivetrain
	 * 
	 * @return whether the robot should shift to low gear
	 */
	public boolean currentControl() {
		int rightChannel = (int) (Robot.getPref("Drivetrain PDP Channel Right", 13));
		int leftChannel = (int) (Robot.getPref("Drivetrain PDP Channel Left", 15));
		double rightCurrent = pdp.getCurrent(rightChannel);
		double leftCurrent = pdp.getCurrent(leftChannel);
		if (rightCurrent >= Robot.getPref("drivetrainMaxCurrentRight", 110)
				|| leftCurrent >= Robot.getPref("drivetrainMaxCurrentLeft", 110))
			return true;
		return false;
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
	
	/*
	 * (non-Javadoc)
	 * @see org.usfirst.frc199.Robot2017.DashboardInterface#displayData()
	 */
	public void shiftLow(){
		shiftPiston.set(DoubleSolenoid.Value.kReverse);
	}

	@Override
	public void displayData() {
		putNumber("Average Speed", getVelocity());
		
		putNumber("Left Joystick Y", Robot.oi.leftJoy.getY());
		putNumber("Right Joystick Y", Robot.oi.rightJoy.getY());
		
		putNumber("Left Distance", leftEncoder.getDistance());
		putNumber("Right Distance", rightEncoder.getDistance());
		putNumber("Average Distance", getDistance());
		
		putNumber("Acceleration", (getVelocity() - prevEncoderRate) / (Timer.getFPGATimestamp() - prevTime));
		putNumber("Angular acceleration",
				(getAngularVelocity() - prevGyroRate) / (Timer.getFPGATimestamp() - prevTime));

		putNumber("Angle", gyro.getAngle());
		putNumber("Turn Speed", gyro.getRate());

		putNumber("Left DT Signal", leftMotor.get());
		putNumber("Right DT Signal", rightMotor.get());

		putNumber("PDP_Left_Drive", pdp.getCurrent(13));
		putNumber("PDP_Right_Drive", pdp.getCurrent(15));
		SmartDashboard.putNumber("Left enc speed", leftEncoder.getRate());
		SmartDashboard.putNumber("Right enc speed", rightEncoder.getRate());
		putNumber("Sending to left motor", leftMotor.getRaw());
		putNumber("Sending to right motor", rightMotor.getRaw());
		putString("Current drive", currentDrive.toString());
		putNumber("Right US Voltage", rightUSsensor.getVoltage());
		putNumber("Left US Voltage", leftUSsensor.getVoltage());
		putNumber("Avg US Voltage", (getUSVoltage(true) + getUSVoltage(false)) / 2);
		putNumber("US Dist to Drive", getUSDistToDrive());
		putNumber("US Angle", getUSTargetAngle());

		prevEncoderRate = getVelocity();
		prevGyroRate = getAngularVelocity();
		prevTime = Timer.getFPGATimestamp();
	}

	/**
	 * returns whether or not the AnalogInput detects an object blocking the
	 * light
	 */
	public double getAverageMotors() {
		return (Math.abs(leftMotor.get()) + Math.abs(rightMotor.get())) / 2;
	}
}
