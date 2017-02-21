package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

/**
 *
 */
public class RunIntake extends Command {
	double speed;
	IntakeInterface intake;

	public RunIntake(double speed, IntakeInterface intake) {
//		requires(Robot.intake);
		this.speed = speed;
		this.intake = intake;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if(intake.intakeIsUp()){
			if (!intake.intakeCurrentOverflow()) {
				intake.controlledIntake(speed);
			}
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	public void end() {
		intake.runIntake(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		intake.runIntake(0);
	}
}
