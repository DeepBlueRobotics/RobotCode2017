package org.usfirst.frc199.Robot2017.motion;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public interface PIDInterface {

	/**
	 * Sets the target value of the loop
	 * 
	 * @param value - The target value in real units
	 */
	public void setTarget(double value);
	
	public void setTargetNotTotError(double value);

	/**
	 * Updates state based on a new input value
	 * 
	 * @param newValue - new input value in real units
	 */
	public void update(double newValue);

	/**
	 * Sets the relative value of the current location
	 * 
	 * @param value - The desired value of the current location
	 */
	public void setRelativeLocation(double value);

	/**
	 * Gets the computed PID output
	 * 
	 * @return - The computed PID output
	 */
	public double getOutput();

	/**
	 * Gets the current target
	 * 
	 * @return - The current target
	 */
	public double getTarget();

	/**
	 * Determines if target has been reached
	 * 
	 * @return True if error and rate are within acceptable tolerances
	 */
	public boolean reachedTarget();

	public void displayData();
}
