package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;

/**
 *
 */
public class AutoModeBasic extends Command {

	Timer tim = new Timer();
	private double time;

	public AutoModeBasic(double timeLimit) {
		this.time = timeLimit;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drivetrain.arcadeDrive(.7, 0);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return tim.get() > time;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drivetrain.stopDrive();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
