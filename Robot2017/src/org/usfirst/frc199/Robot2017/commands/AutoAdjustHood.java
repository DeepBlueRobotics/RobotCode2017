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
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	// all distances are in inches
    	// actuator measurements
    	double closedLength = 4.64567;
    	double stroke = 1.9685;
    	
    	// all angles are in radians
    	// target angle from ground for the ball to exit
    	double targetShootAngle = 30;
    	// components of pistonDistance
    	double pistonBottomX = 2.930586;
    	double pistonBottomY = 2.1351762;
    	// target angle for angle piston/hood-hood pivot-piston-body
    	double targetPistonAngle = Math.atan(pistonBottomY/pistonBottomX) - targetShootAngle + Math.PI/2;
    	// distance from hood-body pivot to piston-hood pivot
    	double hoodHeight = 6.5005459;
    	// distance from hood-body pivot to piston-body pivot
    	double pistonDistance = 3.6258839;
    	// length the piston should be, from piston-body piston to piston-hood piston
    	double pistonHeight = Math.sqrt(
    			// ( a^2 + b^2
    			hoodHeight * hoodHeight + pistonDistance * pistonDistance 
    			// - 2ab*cos(C) )
    			- 2 * hoodHeight * pistonDistance * Math.cos(targetPistonAngle));
    	// the value (0.0 to 1.0) to pass to .set()
    	double pistonArgument = (pistonHeight - closedLength)/stroke;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        return shooter.hoodPIDTargetReached();
    }

    // Called once after isFinished returns true
    public void end() {
//    	shooter.stopHoodMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}