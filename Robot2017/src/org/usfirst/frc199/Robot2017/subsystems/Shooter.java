package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.PID;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
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
public class Shooter extends Subsystem implements DashboardInterface, ShooterInterface {
	double shootingAngle = Robot.getPref("shootingAngle", 0);
	double height = Robot.getPref("relativeHeightOfBoiler", 0);
	double encoderAngleRatio = Robot.getPref("encoderAngleRatio", 0);

	private final SpeedController shootMotor = RobotMap.shooterShootMotor;
	private final SpeedController feedMotor = RobotMap.shooterFeedMotor;
	private final Encoder shootEncoder = RobotMap.shooterShootEncoder;
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

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
	}

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled() {

		if (Math.abs(shootMotor.get()) >= 0.2 && (shootEncoder.get() - prevShooterEncoder) <= Robot.getPref("encoderOffset", 5)
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
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor(double speed) {
		shootMotor.set(speed);
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
	 * Tells the shooter motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for shooter motor PID
	 */
	public void setShooterPIDTarget(double targetRate) {
		ShooterPID.setTarget(targetRate);
	}

	/**
	 * Updates the shooter motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue current
	 *            shooter motor encoder speed
	 */
	public void updateShooterPID(double updateValue) {
		ShooterPID.update(updateValue);
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
	 * 
	 * @param target - the target value for PID
	 * @return speed of motor
	 */
	public double updateSpeed(double target){
		ShooterPID.setTarget(target);
		ShooterPID.update(currentSpeed());
		return ShooterPID.getOutput();
	}

/**
	 * Returns the current speed of the shooter wheel.
	 * @return shooter speed in inches per second
	 */
	public double currentSpeed(){
		return shootEncoder.getRate();
	}

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in inches of the front of the robot from the boiler
	 * @return the ideal exit speed of the ball in inches/second
	 */
	public double convertDistanceToTargetSpeed(double distance) {
		return distance / Math.cos(shootingAngle)
				* Math.sqrt(386.09 / (2 * (distance * Math.tan(shootingAngle) - height)));
	}
	
	
	//come up with PID methods (for turret and hood) similar to those of shooter
	//turret should use vision
	//hood should convert real angles to encoder values (test to find a ratio)
	
	
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
	 * Tells the hood motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for hood motor PID
	 */
	public void setHoodPIDTarget(double target) {
		HoodPID.setTarget(target);
	}

	/**
	 * Updates the hood motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue current
	 *            hood motor encoder speed
	 */
	public void updateHoodPID(double updateValue) {
		HoodPID.update(updateValue);
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
	 * Sets the hood motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the hood motor
	 */
	public void runHoodMotor(double speed) {
		hoodMotor.set(speed);
	}
	
	public double convertAngleToTargetSpeed(double targetAngle){
		//do some math
		double angle = targetAngle;
		double encoderVal = angle*encoderAngleRatio;
		return 0.0;
	}

	@Override
	public void displayData() {
		SmartDashboard.putBoolean("Shooter motor stalled", shooterMotorStalled);
		SmartDashboard.putNumber("Shoot encoder rate", shootEncoder.getRate());
		SmartDashboard.putNumber("Shooter PID output", getShooterPIDOutput());
		SmartDashboard.putNumber("Turret encoder value", turretEncoder.get());
		SmartDashboard.putNumber("Hood encoder value", hoodEncoder.get());
	}
}
