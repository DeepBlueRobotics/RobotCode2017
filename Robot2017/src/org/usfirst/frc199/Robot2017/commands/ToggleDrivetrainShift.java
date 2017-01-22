package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class ToggleDrivetrainShift extends Command {
	DrivetrainInterface toggleGear;
	public ToggleDrivetrainShift(DrivetrainInterface toggleGear) {
		this.toggleGear = toggleGear;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		this.toggleGear.shiftGears();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
