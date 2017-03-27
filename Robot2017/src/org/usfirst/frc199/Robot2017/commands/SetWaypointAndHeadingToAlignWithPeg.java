package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Log;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetWaypointAndHeadingToAlignWithPeg extends Command {
	
	private WaypointAndHeading w;
	
	
	
    public SetWaypointAndHeadingToAlignWithPeg(WaypointAndHeading w) {
    	this.w = w;
    
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	SmartDashboard.putBoolean("Vision/gearRunning", true);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return SmartDashboard.getBoolean("Vision/OH-YEAH", false);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Log.debug("SetWaypointAndHeadingToAlignWithPeg.end() called");
    	Robot.vision.updateGearCoordinates();
    	double[] leftTarget = Robot.vision.leftMarkCoords;
    	Log.debug("leftTarget: " + " (" + leftTarget[0] + ", " + leftTarget[1] + ")");
    	double[] rightTarget = Robot.vision.rightMarkCoords;
    	Log.debug("rightTarget: " + " (" + rightTarget[0] + ", " + rightTarget[1] + ")");
    	storeWaypointAndHeading(w, leftTarget, rightTarget);
    	SmartDashboard.putBoolean("Vision/gearRunning", false);
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
    
    protected double getTargetDistanceFromLift() {
    	return SmartDashboard.getNumber("AutoDeliverGear/StandoffDistance", 24);
    }
    
    private void storeWaypointAndHeading(WaypointAndHeading w, double[] leftTarget, double[] rightTarget) {
    	double[] lift = {(leftTarget[0] + rightTarget[0]) / 2, (leftTarget[1] + rightTarget[1]) / 2};
    	double d = getTargetDistanceFromLift();
    	double[] vector = {rightTarget[1]-leftTarget[1], -rightTarget[0]+leftTarget[0]};
    	double magnitude = Math.sqrt(Math.pow(leftTarget[1]-rightTarget[1],2)+Math.pow(leftTarget[0]-rightTarget[0],2));
    	double[] p = {lift[0] + (d * vector[0]) / magnitude, lift[1] + (d * vector[1]) / magnitude};
    	w.distanceToWaypoint = Math.sqrt(Math.pow(p[0], 2)+Math.pow(p[1],2));
    	Log.debug("distanceToWaypoint set to " + w.distanceToWaypoint);
    	w.headingToWaypoint = Math.toDegrees(Math.atan(p[0]/p[1]));
    	Log.debug("headingToWaypoint set to " + w.headingToWaypoint);
    	w.newHeadingAtWaypoint = Math.toDegrees(Math.atan(vector[0]/vector[1]));
    	Log.debug("newHeadingAtWaypoint set to " + w.newHeadingAtWaypoint);
    }
    
}
