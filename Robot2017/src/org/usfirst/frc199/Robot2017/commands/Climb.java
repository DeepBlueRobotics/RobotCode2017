package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ClimberInterface;

/**
 * Actuates the climber motor in only one direction
 * 
 * FUNCTIONAL
 */
public class Climb extends Command {

	boolean isStopped = false;
	ClimberInterface climber;
	boolean backwards;
	double speed;

	public Climb(ClimberInterface climber, double speed) {
		this.climber = climber;
		requires(Robot.climber);
		this.speed = speed;
	}
	
	public void initialize() {

	}

	public void execute() {

		if (!climber.checkMotorDraw()) {
			climber.runClimber(speed);
		} else {
			climber.stopWinch();
		}

	}

	protected boolean isFinished() {
		return false;
	}

	public void end() {
		climber.stopWinch();
	}

	protected void interrupted() {
		end();
	}
}
