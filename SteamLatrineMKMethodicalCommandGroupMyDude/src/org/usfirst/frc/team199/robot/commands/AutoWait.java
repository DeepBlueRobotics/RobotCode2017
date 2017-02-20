package org.usfirst.frc.team199.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoWait extends Command {

	double duration;
	Timer tim = new Timer();
    public AutoWait(double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	duration = time;
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
        return tim.get() >= duration;
    }

    // Called once after isFinished returns true
    protected void end() {
    	SmartDashboard.putBoolean("ayyyy", true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
