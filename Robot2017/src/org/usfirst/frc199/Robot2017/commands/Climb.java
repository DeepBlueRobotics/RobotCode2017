package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ClimberInterface;

public class Climb extends Command {

	boolean isStopped = false;
	ClimberInterface climber;

	public Climb(ClimberInterface climber) {
		this.climber = climber;
		requires(Robot.climber);
	}

	// Called just before this Command runs the first time
	public void initialize() {
//		climber.encoderReset();
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
//		if (climber.getEncoder() >= 48 && climber.getAIEnabled() == false) {
//			climber.setAIEnabled(!climber.getAIEnabled());
//		}
//		if (!climber.returnPlate()) {
//			isStopped = false;
//			if (!climber.checkMotorDraw()) {
//				climber.runClimber(Robot.getPref("defaultClimberSpeed", 1));
//			} else if (climber.getClimber() > Robot.getPref("climberSlowStep", 0.005)) {
//				climber.runClimber(climber.getClimber() - Robot.getPref("climberSlowStep", 0.005));
//			} else {
//				climber.stopWinch();
//			}
//		} else if (!isStopped) {
//			isStopped = true;
//			climber.stopWinch();
//		}

		if (!climber.checkMotorDraw()) {
			climber.runClimber(Robot.getPref("defaultClimberSpeed", 1));
		} else if (climber.getClimber() > Robot.getPref("climberSlowStep", 0.005)) {
			climber.runClimber(climber.getClimber() - Robot.getPref("climberSlowStep", 0.005));
		} else {
			climber.stopWinch();
		}
		// if(climber.returnPlate()) {
		// climber.stopWinch();
		// } else {
		// climber.runClimber(getPref("defaultClimberSpeed", 1));
		// }

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	public void end() {
		climber.stopWinch();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		climber.stopWinch();
	}
}
