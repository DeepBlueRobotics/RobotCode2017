package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

public interface ShooterInterface extends DashboardInterface {
	public void initDefaultCommand();

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled();
	
	/**
	 * Gets the shooter encoder rate
	 * @return the shooter encoder rate
	 * */
	public double getShootEncoderRate();

	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor(double speed);
	
	/**
	 * Stops the shooter motor
	 * */
	public void stopShootMotor();

	/**
	 * Sets the feeder motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the feeder motor
	 */
	public void runFeederMotor(double speed);

	/**
	 * Tells the shooter motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for shooter motor PID
	 */
	public void setShooterPIDTarget(double targetRate);

	/**
	 * Updates the shooter motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current shooter motor encoder speed
	 */
	public void updateShooterPID(double updateValue);

	/**
	 * Gets the speed for the shooter motor from the shooter PID.
	 * 
	 * @return speed for motor
	 */
	public double getShooterPIDOutput();
	
	/**
	 * Gets the hood encoder value
	 * @return the hood encoder value
	 * */
	public double getHoodEncoder();

	/**
	 * 
	 * @param target
	 *            - the target value for PID
	 * @return speed of motor
	 */
	public double updateSpeed(double target);
	
	/**
	 * Stops the hood motor
	 * */
	public void stopHoodMotor();

	/**
	 * Returns the current speed of the shooter wheel.
	 * 
	 * @return shooter speed in inches per second
	 */
	public double currentSpeed();

	public double convertDistanceToTargetVelocity(double distance);
	
	public double convertDistanceToTargetAngle(double distance);

	public void runTurretMotor(double speed);

	public void setHoodPIDTarget(double target);

	public void updateHoodPID(double updateValue);

	public double getHoodPIDOutput();

	public void runHoodMotor(double speed);
}
