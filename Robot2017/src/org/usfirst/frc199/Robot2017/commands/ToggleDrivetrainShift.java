package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

/**
 *
 */
public class ToggleDrivetrainShift extends Command {
	DrivetrainInterface toggleGear;
	boolean firstTime = true;
	public ToggleDrivetrainShift(DrivetrainInterface toggleGear) {
		this.toggleGear = toggleGear;
	}
	
	Timer tim = new Timer();
	IntakeInterface intake;

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.start();
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if(firstTime) {
			this.toggleGear.shiftGears();
			firstTime = false;
		}
		
		        
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return (tim.get() >= 1);
    	
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
