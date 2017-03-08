package org.usfirst.frc199.Robot2017.motion;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a PID loop and automatically generates new SmartDashboard values and
 * preferences.
 */
public class PID implements DashboardInterface {

	// Parameters (provided by user):
	private double target; // The value PID will attempt to reach (setpoint)
	private double kP; // Proportional factor
	private double kI; // Integral factor
	private double kD; // Derivative factor
	private final String name; // The name of the loop for preferences and
								// SmartDashboard

	// Current state:
	private Timer intervalTimer = new Timer(); // Keeps track of loop frequency
	private double input; // Current input value
	private double error; // Current error value
	private double lastError; // Previous error value
	private double totalError; // Integral of error over time
	private double output; // The computed PID output
	private double rate; // Change in error over time
	private double interval; // Time elapsed since last update
	private double offset = 0; // Sets what the "zero" value is
	private boolean reset; // Flag indicating loop needs to be reset

	/**
	 * Creates a new PID loop
	 * 
	 * @param name - The identifier of the loop
	 */
	public PID(String name) {
		this.name = name;
		Robot.subsystems.add(this);
		intervalTimer.start();
		putNumber("kP", getPref("kP", 0));
		putNumber("kI", getPref("kI", 0));
		putNumber("kD", getPref("kD", 0));
		putNumber("TestTarget", 0);
	}

	/**
	 * Sets the target value of the loop
	 * 
	 * @param value - The target value in real units
	 */
	public void setTarget(double value) {
		target = value;
		reset = true;
		output = 0.0;
	}

	public void setTarget(double value, boolean reset) {
		target = value;
		if (reset) {
			this.reset = true;
			output = 0.0;
			totalError = 0.0;
		}
	}

	/**
	 * Updates state based on a new input value
	 * 
	 * @param newValue - new input value in real units
	 */
	public void update(double newValue) {
		if (name.toLowerCase().contains("velocity")) {
			kP = getNumber("kP", 0);
			if (Math.abs(target) <= Robot.getPref("Velocity PID deadband", .03)) {
				kI = 0;
			} else {
				kI = 1 / (Math.abs(target));
			}

			putNumber("kI", kI);
		} else {
//			this happens if is a distance or angle PID
			kP = 1 / (Math.abs(target));
			putNumber("kP", kP);
			kI = getNumber("kI", 0);
		}

//		kP = getNumber("kP", 0);
		kI = getNumber("kI", 0);
		kD = getNumber("kD", 0);
		input = newValue - offset;
		error = target - input;
		interval = intervalTimer.get();
		intervalTimer.reset();
		output = kP * error;
		if (interval > 0.0 && interval < 1.0 && !reset) {
			totalError += error * interval;
			rate = (error - lastError) / interval;
			output += kI * totalError + kD * rate;
//			output += kI * totalError + kD * (error - lastError);
		} else {
			reset = false;
			totalError = 0;
			rate = 0;
		}
		lastError = error;
	}

	/**
	 * Sets the relative value of the current location
	 * 
	 * @param value - The desired value of the current location
	 */
	public void setRelativeLocation(double value) {
		offset = value;
	}

	/**
	 * Gets the computed PID output
	 * 
	 * @return - The computed PID output
	 */
	public double getOutput() {
		return output;
	}

	/**
	 * Gets the computed PID error
	 * 
	 * @return - The computed PID error
	 */
	public double getError() {
		return error;
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
		return Math.abs(lastError) < getPref("ErrorTolerance", 0.5) && Math.abs(rate) < getPref("RateTolerance", 0.5);
//		return Math.abs(lastError) < 0.05 && Math.abs(rate) < 0.05;
	}

	/**
	 * Gets a preference for the specific instance of the PID class
	 * 
	 * @param key - The name of the preference
	 * @return The value of the preference
	 */
	private double getPref(String key, double defaultValue) {
		return Robot.getPref(name + key, defaultValue);
	}

	@Override
	public String getKey(String originalKey) {
		return "PID/" + name + "/" + originalKey;
	}

	public void displayData() {
		putNumber("Error", error);
		putNumber("Target", target);
		putNumber("Input", input);
		putNumber("Output", output);
		putNumber("Interval", interval);
		putNumber("Rate", rate);
		putNumber("TotalError", totalError);
		putBoolean("Reached Target", reachedTarget());
	}
}