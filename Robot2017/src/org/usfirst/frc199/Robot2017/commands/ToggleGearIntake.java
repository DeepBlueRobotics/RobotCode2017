
package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

/**
 *
 */
public class ToggleGearIntake extends Command {
	IntakeInterface intake;
	boolean firstTime = true;
	boolean giveDirection = false;
	boolean down = true;
	Timer tim;
	
	/**
	 * @param giveDirection you want a specific direction to be set, not just toggle to whatever
	 * @param down you want the intake to go down (only used when giveDirection true)
	 * */
	public ToggleGearIntake(boolean giveDirection, boolean down, IntakeInterface intake) {
		requires(Robot.intake);
		this.intake = intake;
		this.giveDirection = giveDirection;
		this.down = down;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim = new Timer();
		firstTime = true;
		tim.start();
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if (firstTime) {
			intake.toggleIntake(giveDirection, down);
			firstTime = false;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return tim.get() >= 0.0625;

	}

	// Called once after isFinished returns true
	public void end() {
		intake.setIntakePistonNeutral();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
