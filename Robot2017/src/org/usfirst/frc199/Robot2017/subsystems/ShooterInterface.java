package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

public interface ShooterInterface extends DashboardInterface {
	public void initDefaultCommand();
	
	/**
	 * Sets the feeder motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the feeder motor
	 */
	public void runFeederMotor(double speed);
	
	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * For PID AutoShoot (AutoShoot2)
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor2(double speed);
	
	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0).
	 * For CANTalon AutoShoot
	 * 
	 * @param rate
	 *            - speed to give the shooter motor
	 */
	public void runShootMotor(double speed);

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * For PID AutoShoot (AutoShoot2)
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled2();
	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works
	 * For CANTalon AutoShoot
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled();

	/**
	 * Returns the current speed of the shooter wheel.
	 * For PID AutoShoot (AutoShoot2)
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed2();
	
	/**
	 * Returns the current speed of the shooter wheel.
	 * For CANTalon AutoShoot
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed();

	/**
	 * Tells the shooter motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for shooter motor PID
	 */
	public void setShooterPIDTarget(double targetRate);

	/**
	 * Gets the speed for the shooter motor from the shooter PID.
	 * 
	 * @return speed for motor
	 */
	public double getShooterPIDOutput();

	/**
	 * Updates the shooter motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current shooter motor encoder speed
	 */
	public void updateShooterPID(double updateValue);

	/**
	 * Stops the shooter motor
	 */
	public void stopShootMotor();

	/**
	 * Used only in TestPID
	 * @param target
	 *            - the target value for PID
	 * @return speed of motor
	 */
	public double updateSpeed(double target);

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in inches of the front of the robot from the boiler
	 * @return the ideal exit speed of the ball in inches/second
	 */
	public double convertDistanceToTargetVelocity(double distance);

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in inches of the front of the robot from the boiler
	 * @return the ideal exit angle of the ball in radians
	 */
	public double convertDistanceToTargetAngle(double distance);

	/**
	 * Gets the turret encoder value
	 * 
	 * @return the turret encoder value
	 */
	public double getTurretEncoder();
	
	/**
	 * Sets the turret motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the turret motor
	 */
	public void runTurretMotor(double speed);

	/**
	 * Tells the turret motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for turret motor PID
	 */
	public void setTurretPIDTarget(double target);

	/**
	 * Gets the speed for the turret motor from the turret PID.
	 * 
	 * @return speed for motor
	 */
	public double getTurretPIDOutput();

	/**
	 * Updates the turret motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current turret motor encoder speed
	 */
	public void updateTurretPID(double updateValue);
	
	/**
	 * Gets if turret PID target reached or not.
	 * @return turret PID target is reached or not
	 * */
	public boolean turretPIDTargetReached();

	/**
	 * Stops the turret motor
	 */
	public void stopTurretMotor();

	/**
	 * Gets the hood encoder value
	 * 
	 * @return the hood encoder value
	 */
	public double getHoodEncoder();

	/**
	 * Sets the hood motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate
	 *            - speed to give the hood motor
	 */
	public void runHoodMotor(double speed);
	
	/**
	 * Tells the hood motor's PID the target speed to reach.
	 * 
	 * @param targetRate
	 *            - target speed for hood motor PID
	 */
	public void setHoodPIDTarget(double target);

	/**
	 * Gets the speed for the hood motor from the hood PID.
	 * 
	 * @return speed for motor
	 */
	public double getHoodPIDOutput();

	/**
	 * Updates the hood motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue
	 *            current hood motor encoder speed
	 */
	public void updateHoodPID(double updateValue);
	
	/**
	 * Gets if hood PID target reached or not.
	 * @return hood PID target is reached or not
	 * */
	public boolean hoodPIDTargetReached();

	/**
	 * Stops the hood motor
	 */
	public void stopHoodMotor();

	@Override
	public void displayData();
}
