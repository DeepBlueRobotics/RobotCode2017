package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAdjustHood extends Command {
	
	double target;
	ShooterInterface shooter;
	
    public AutoAdjustHood(ShooterInterface shooter) {
        //IS WRONG! NEEDS TO CHANGE!
    	//target = Robot.shooter.convertAngleToTargetSpeed(targetAngle);
    	this.shooter = shooter;
    }

    // Called just before this Command runs the first time
    public void initialize() {
    	shooter.setHoodPIDTarget(target);
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	shooter.updateHoodPID(shooter.getHoodEncoder());
    	shooter.runHoodMotor(shooter.getHoodPIDOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return shooter.hoodPIDTargetReached();
    }

    // Called once after isFinished returns true
    public void end() {
    	shooter.stopHoodMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
