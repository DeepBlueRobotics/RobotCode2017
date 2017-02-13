package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
import org.usfirst.frc199.Robot2017.motion.PID;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem implements ShooterInterface {
	double targetAngle = Robot.getPref("targetAngle", 0); // angle that the ball
															// hits the boiler
	double height = Robot.getPref("relativeHeightOfBoiler", 0);
	double encoderAngleRatio = Robot.getPref("encoderAngleRatio", 0);

	private final double gravity = 9.81;
	
	private final SpeedController shootMotor = RobotMap.shooterShootMotor;
	private final SpeedController feedMotor = RobotMap.shooterFeedMotor;
	private final Encoder shootEncoder = RobotMap.shooterShootEncoder;
	private final CANTalon shootMotorAndEnc = RobotMap.shooterShootMotorAndEncoder;
	private final SpeedController turretMotor = RobotMap.turretTurnMotor;
	private final Encoder turretEncoder = RobotMap.turretTurretEncoder;
	private final SpeedController hoodMotor = RobotMap.hoodAngleMotor;
	private final Encoder hoodEncoder = RobotMap.hoodAngleEncoder;

	private PID ShooterPID = new PID("ShooterPID");
	private PID TurretPID = new PID("TurretPID");
	private PID HoodPID = new PID("HoodPID");

	private double prevShooterEncoder = 0;
	private boolean shooterMotorStalled = false;

	private double prevTurretEncoder = 0;
	private double prevHoodEncoder = 0;

	public Shooter(){
		super();
		putString("~TYPE~", "Shooter");
	}
	
	public void initDefaultCommand() {
	}
	
	/**
	 * Sets the feeder motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the feeder motor
	 */
	public void runFeederMotor(double speed) {
		feedMotor.set(speed);
	}
	
	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * For PID AutoShoot (AutoShoot2)
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor2(double speed) {
		shootMotor.set(speed);
	}
	
	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * For CANTalon AutoShoot
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor(double speed){
		double targetSpeed = speed;
		shootMotorAndEnc.setP(1/targetSpeed);
		shootMotorAndEnc.set(targetSpeed);
	}

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * For PID AutoShoot (AutoShoot2)
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled2() {

		if (Math.abs(shootMotor.get()) >= 0.2
				&& (shootEncoder.get() - prevShooterEncoder) <= Robot.getPref("encoderOffset", 5)
				&& Robot.getPref("shooterEncoderWorks", 0) == 1) {
			shootMotor.set(0);
			System.out.println("Shooter Motor stalled, stopping motor.");
			prevShooterEncoder = shootEncoder.get();
			shooterMotorStalled = true;
		} else {
			prevShooterEncoder = shootEncoder.get();
			shooterMotorStalled = false;
		}
		return shooterMotorStalled;
	}
	
	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * For CANTalon AutoShoot
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled() {
		if (Math.abs(shootMotorAndEnc.get()) >= 0.2
				&& (shootMotorAndEnc.getEncPosition() - prevShooterEncoder) <= Robot.getPref("encoderOffset", 5)
				&& Robot.getPref("shooterEncoderWorks", 0) == 1) {
			shootMotorAndEnc.set(0);
			System.out.println("Shooter Motor stalled, stopping motor.");
			shooterMotorStalled = true;
		} else {
			shooterMotorStalled = false;
		}
		prevShooterEncoder = shootMotorAndEnc.getEncPosition();
		return shooterMotorStalled;
	}

	/**
	 * Returns the current speed of the shooter wheel.
	 * For PID AutoShoot (AutoShoot2)
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed2() {
		return shootEncoder.getRate();
	}
	
	/**
	 * Returns the current speed of the shooter wheel.
	 * For CANTalon AutoShoot
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed(){
		return shootMotorAndEnc.getEncVelocity();
	}

	/**
	 * Tells the shooter motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for shooter motor PID
	 */
	public void setShooterPIDTarget(double targetRate) {
		ShooterPID.setTarget(targetRate);
	}

	/**
	 * Gets the speed for the shooter motor from the shooter PID.
	 * 
	 * @return speed for motor
	 */
	public double getShooterPIDOutput() {
		return ShooterPID.getOutput();
	}

	/**
	 * Updates the shooter motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current shooter motor encoder speed
	 */
	public void updateShooterPID(double updateValue) {
		ShooterPID.update(updateValue);
	}

	/**
	 * Stops the shooter motor
	 */
	public void stopShootMotor() {
		runShootMotor(0);
	}

	/**
	 * Used only in TestPID
	 * @param target
	 *            - the target value for PID
	 * @return speed of motor
	 */
	public double updateSpeed(double target) {
		ShooterPID.setTarget(target);
		ShooterPID.update(getShooterSpeed());
		return ShooterPID.getOutput();
	}

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in inches of the front of the robot from the boiler
	 * @return the ideal exit speed of the ball in inches/second
	 */
	public double convertDistanceToTargetVelocity(double distance) { 
		return Math.sqrt(2 * gravity * height + (gravity * distance * distance)
				/ (2 * Math.cos(targetAngle) * Math.cos(targetAngle) * (height + distance * Math.tan(targetAngle))));
	}

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in inches of the front of the robot from the boiler
	 * @return the ideal exit angle of the ball in radians
	 */
	public double convertDistanceToTargetAngle(double distance) {
		double v0 = convertDistanceToTargetVelocity(distance);
		return Math.asin(v0 / (Math.cos(targetAngle) * Math.sqrt(v0 * v0 - 2 * gravity * height)));
	}

	// come up with PID methods (for turret and hood) similar to those of
	// shooter
	// turret should use vision
	// hood should convert real angles to encoder values (test to find a ratio)
	
	/**
	 * Gets the turret encoder value
	 * 
	 * @return the turret encoder value
	 */
	public double getTurretEncoder() {
		return turretEncoder.get();
	}
	
	/**
	 * Sets the turret motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the turret motor
	 */
	public void runTurretMotor(double speed) {
		turretMotor.set(speed);
	}

	/**
	 * Tells the turret motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for turret motor PID
	 */
	public void setTurretPIDTarget(double target) {
		TurretPID.setTarget(target);
	}

	/**
	 * Gets the speed for the turret motor from the turret PID.
	 * 
	 * @return speed for motor
	 */
	public double getTurretPIDOutput() {
		return TurretPID.getOutput();
	}

	/**
	 * Updates the turret motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current turret motor encoder speed
	 */
	public void updateTurretPID(double updateValue) {
		TurretPID.update(updateValue);
	}
	
	/**
	 * Gets if turret PID target reached or not.
	 * @return turret PID target is reached or not
	 * */
	public boolean turretPIDTargetReached(){
		return TurretPID.reachedTarget();
	}

	/**
	 * Stops the turret motor
	 */
	public void stopTurretMotor() {
		runTurretMotor(0);
	}

	/**
	 * Gets the hood encoder value
	 * 
	 * @return the hood encoder value
	 */
	public double getHoodEncoder() {
		return hoodEncoder.get();
	}

	/**
	 * Sets the hood motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the hood motor
	 */
	public void runHoodMotor(double speed) {
		hoodMotor.set(speed);
	}
	
	/**
	 * Tells the hood motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for hood motor PID
	 */
	public void setHoodPIDTarget(double target) {
		HoodPID.setTarget(target);
	}

	/**
	 * Gets the speed for the hood motor from the hood PID.
	 * 
	 * @return speed for motor
	 */
	public double getHoodPIDOutput() {
		return HoodPID.getOutput();
	}

	/**
	 * Updates the hood motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current hood motor encoder speed
	 */
	public void updateHoodPID(double updateValue) {
		HoodPID.update(updateValue);
	}
	
	/**
	 * Gets if hood PID target reached or not.
	 * @return hood PID target is reached or not
	 * */
	public boolean hoodPIDTargetReached(){
		return HoodPID.reachedTarget();
	}

	/**
	 * Stops the hood motor
	 */
	public void stopHoodMotor() {
		runHoodMotor(0);
	}

	@Override
	public void displayData() {
		putBoolean("Shooter motor stalled", shooterMotorStalled);
		putNumber("Shoot encoder rate", shootEncoder.getRate());
		putNumber("Shooter PID output", getShooterPIDOutput());
		putNumber("Turret encoder value", turretEncoder.get());
		putNumber("Hood encoder value", hoodEncoder.get());
	}
}
