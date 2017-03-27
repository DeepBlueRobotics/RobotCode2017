package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PlaceGearOnPeg extends Command {

	IntakeInterface intake;
	Timer tim = new Timer();
    public PlaceGearOnPeg(IntakeInterface intake) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.intake);
    	this.intake = intake;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tim.reset();
    	tim.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
