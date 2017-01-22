package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;

public class Climb extends Command {

	boolean isStopped = false;

	public Climb() {

		requires(Robot.climber);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.climber.encoderReset();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (Robot.climber.getEncoder() >= 48 && Robot.climber.AIEnabled == false) {
			Robot.climber.AIEnabled = !Robot.climber.AIEnabled;
		}
		if (!Robot.climber.returnPlate()) {
			isStopped = false;
			if (!Robot.climber.checkMotorDraw()) {
				Robot.climber.runClimber(Robot.getPref("defaultClimberSpeed", 1));
			} else if (Robot.climber.getClimber() > 0.3) {
				Robot.climber.runClimber(Robot.climber.getClimber() - 0.3);
			} else {
				Robot.climber.stopWinch();
			}
		} else if (!isStopped) {
			isStopped = true;
			Robot.climber.stopWinch();
		}

		// if(Robot.climber.returnPlate()) {
		// Robot.climber.stopWinch();
		// } else {
		// Robot.climber.runClimber(Robot.getPref("defaultClimberSpeed", 1));
		// }

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.climber.stopWinch();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
