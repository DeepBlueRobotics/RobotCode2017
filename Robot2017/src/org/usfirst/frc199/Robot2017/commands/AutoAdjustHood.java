package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAdjustHood extends Command {
	
	double target;
	
    public AutoAdjustHood() {
        //IS WRONG! NEEDS TO CHANGE!
    	//target = Robot.shooter.convertAngleToTargetSpeed(targetAngle);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setHoodPIDTarget(target);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooter.updateHoodPID(RobotMap.hoodAngleEncoder.get());
    	Robot.shooter.runHoodMotor(Robot.shooter.getHoodPIDOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.runHoodMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
