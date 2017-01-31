package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class ToggleDrivetrainShift extends Command {
	
	DrivetrainInterface drivetrain;
	Timer tim = new Timer();
	
	public ToggleDrivetrainShift(DrivetrainInterface drivetrain) {
		this.drivetrain = drivetrain;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.start();
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		this.drivetrain.shiftGears();
		while( tim.get() < 0.0625){}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return tim.get() >= 0.0625;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
