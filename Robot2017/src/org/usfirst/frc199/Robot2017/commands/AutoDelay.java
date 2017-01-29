package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDelay extends Command {
	
	Timer tim = new Timer();
	private double time;
	IntakeInterface intake;
	
	//can use either time limit or the end condition of gear being lifted for this command
	//if using time limit, just enter the time
	//if using end condition, enter 0 for time
    public AutoDelay(double time, IntakeInterface intake) {
        this.time = time;
        this.intake = intake;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tim.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(time != 0.0){
    		if((tim.get() >= time))
    		{
    			return true;
    		}
    	} else if(intake.gearLifted()){
    		return true;
    	}
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
