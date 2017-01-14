package org.usfirst.frc199.Robot2017.motion;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Information:
 * 
 * Current velocity Current acceleration Distance traveled Distance to target
 * 
 * To return: velocity output for any direction of motion
 *
 */

public class TrapezoidalControl implements DashboardInterface{
	private double target;
	private final String name; // The name of the loop for preferences and
								// SmartDashboard

	// Current state:
	private Timer motionTimer = new Timer(); // Keeps track of time
	private double input; // Current input value
	private double error; // Current error value
	private double lastError; // Previous error value
	//private double totalError; // Integral of error over time
	private double output; // The computed TrapezoidalControl output
	private double rate; // Change in error over time
	//private double interval; // Time elapsed since last update
	private double maxspeed; // Maximum rate
	private double accel; // Change in rate over time
	private double offset; // Sets what the "zero" value is
	//private boolean reset; // Flag indicating loop needs to be reset

	/**
	 * Creates a new TrapezoidalControl loop
	 * @param name - The identifier of the loop
	 */
	public TrapezoidalControl(String name) {
		this.name = name;
		Robot.subsystems.add(this);
		motionTimer.start();
		putNumber("MaxSpeed", getPref("MaxSpeed"));
		putNumber("Acceleration", getPref("Acceleration"));
		putNumber("TestTarget", 0);
	}

	/**
	 * Sets the target value of the loop
	 * 
	 * @param value
	 *            - The target value in real units
	 */
	public void setTarget(double value) {
		target = value;
		//reset = true;
		output = 0.0;
	}

	/**
	 * Updates state based on a new input value
	 * velocity = min(t*accel, max_speed, sqrt(2*error*accel))
	 * 
	 * @param newValue
	 *            - new input value in real units
	 */
	public void update(double newValue) {
		accel = getNumber("Acceleration", 0);
		maxspeed = getNumber("MaxSpeed", 0);
		input = newValue - offset;
		error = target - input;
		double time = motionTimer.get();
		output = Math.min(Math.min(time*accel, maxspeed), Math.sqrt(2*error*accel));
		lastError = error;
		SmartDashboard.putNumber(name + "Error", error);
		SmartDashboard.putNumber(name + "Output", output);
	}

	/**
	 * Sets the relative value of the current location
	 * 
	 * @param value
	 *            - The desired value of the current location
	 */
	public void setRelativeLocation(double value) {
		offset += input - value;
		input = value;
		setTarget(target);
	}

	/**
	 * Gets the computed TrapezoidalControl output
	 * 
	 * @return - The computed TrapezoidalControl output
	 */
	public double getOutput() {
		return output;
	}

	/**
	 * Gets the current target
	 * 
	 * @return - The current target
	 */
	public double getTarget() {
		return target;
	}

	/**
	 * Determines if target has been reached
	 * 
	 * @return True if error and rate are within acceptable tolerances
	 */
	public boolean reachedTarget() {
		return Math.abs(lastError) < getPref("ErrorTolerance");// && Math.abs(rate) < getPref("RateTolerance");
	}

	/**
	 * Gets a preference for the specific instance of the TrapezoidalControl class
	 * 
	 * @param key
	 *            - The name of the preference
	 * @return The value of the preference
	 */
	private double getPref(String key) {
		return Robot.getPref(name + key, 0.0);
	}

	@Override
	public String getKey(String originalKey) {
		return "TrapezoidalControl/" + name + "/" + originalKey;
	}

	public void displayData() {
		putNumber("Error", error);
		putNumber("Target", target);
		putNumber("Input", input);
		putNumber("Output", output);
		putNumber("MaxSpeed", maxspeed);
		putNumber("Rate", rate);
		putNumber("Acceleration", accel);
		putBoolean("Reached Target", reachedTarget());
	}

}
