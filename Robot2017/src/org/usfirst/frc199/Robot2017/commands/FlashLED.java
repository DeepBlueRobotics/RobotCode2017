package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FlashLED extends Command {
	
	private IntakeInterface intake;
	private Timer tim;
	private int flashCount;
	
    public FlashLED(IntakeInterface intake) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.intake = intake;
    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tim = new Timer();
    	tim.start();
    	tim.reset();
    	flashCount = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	intake.LEDOn();
    	if(tim.get() > 0.5){
    		intake.LEDOn();
    		tim.reset();
    		flashCount++;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        return flashCount > 5;
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.LEDOn();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	intake.LEDOn();
    }
}
