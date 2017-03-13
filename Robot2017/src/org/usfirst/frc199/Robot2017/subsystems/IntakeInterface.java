package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public interface IntakeInterface extends DashboardInterface {

	public void initDefaultCommand();

	/**
	 * Returns the current value of the intakeMotor
	 */
	public double getIntake();

	/**
	 * Rruns the intake motor at a set speed
	 * 
	 * @param speed - the speed you want the intake motor to run at -1 -> 1
	 */
	public void runIntake(double speed);

	/**
	 * Runs intake at a speed based on drive speed
	 */
	public void controlledIntake(boolean isBackwards);

	/**
	 * Returns whether or not the pdp detects the intake drawing more current
	 * than allowed
	 */
	public boolean intakeCurrentOverflow();

	/**
	 * Stops the intake motor
	 */
	public void stopIntake();

	/**
	 * Moves the intake up if it is down, and vice versa
	 */
	public void toggleIntake(boolean giveDirection, boolean down);

	/**
	 * Sets the pivot piston to neutral
	 */
	public void setIntakePistonNeutral();

	/**
	 * @return if the intake is up or not
	 */
	public boolean intakeIsDown();

	/**
	 * Sets flipperFlapper to forward unless it already is, then sets to
	 * backwards
	 */
	public void toggleFlipperFlapper();

	/**
	 * Sets the flipperFlapper to neutral
	 */
	public void setFlipperFlapperNeutral();

	/**
	 * @return if the gear has been lifted or not
	 */
	public boolean gearLifted(boolean isTriggered);

	/**
	 * Resets the light sensor trigger value
	 */
	public void resetAITrigger();

	@Override
	/**
	 * Displays data to SmartDashboard
	 */
	public void displayData();
}
