package org.usfirst.frc.team199.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team199.robot.Robot;

/**
 *
 */
public class ExampleCommand extends Command {
	Timer tim = new Timer();
	double time;
	public ExampleCommand(double time) {
		this.time = time;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		tim.reset();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		SmartDashboard.putBoolean("booler", false);
		return tim.get() >= time;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
