
package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

/**
 *
 */
public class ToggleIntake extends Command {
	IntakeInterface intake;
	boolean firstTime = true;
	Timer tim = new Timer();

	public ToggleIntake(IntakeInterface intake) {
		requires(Robot.intake);
		this.intake = intake;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		firstTime = true;
		tim.start();
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if (firstTime) {
			intake.toggleIntake();
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
	}
}
