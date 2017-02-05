package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

/**
 *
 */
public class ToggleDrivetrainShift extends Command {
	
	DrivetrainInterface drivetrain;
	boolean firstTime = true;
	
	public ToggleDrivetrainShift(DrivetrainInterface drivetrain) {
		this.drivetrain = drivetrain;
		
	}
	
	Timer tim = new Timer();
	IntakeInterface intake;

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.start();
		firstTime = true;
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if(firstTime) {
			this.drivetrain.shiftGears();
			firstTime = false;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return !firstTime;
    	
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
