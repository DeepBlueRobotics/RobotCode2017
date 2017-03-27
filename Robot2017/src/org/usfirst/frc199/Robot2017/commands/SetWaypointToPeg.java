package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetWaypointToPeg extends SetWaypointAndHeadingToAlignWithPeg {
	
    public SetWaypointToPeg(WaypointAndHeading w) {
    	super(w);
    }

	 protected double getTargetDistanceFromLift() {
	    	return Robot.getPref("Distance from pivot point to front of robot", 25);
	 }
}
