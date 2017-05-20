package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Second version of a top-level command group for autonomously approaching any lift 
 * and delivering a gear with vision. Most important chunk can be found in 
 * SetWaypointAndHeadingToAlignWithPeg(). 
 * 
 * NOT FUNCTIONAL
 */
public class AutoDeliverGear extends CommandGroup {

    public AutoDeliverGear(DrivetrainInterface drivetrain, IntakeInterface intake) {
    	
    	WaypointAndHeading w = new WaypointAndHeading();
    	
    	//finds waypoint
    	addSequential(new SetWaypointAndHeadingToAlignWithPeg(w));
    	
    	//turns to waypoint
    	addSequential(new TurnToWaypoint(w,drivetrain));
    	
    	//drives to waypoint
    	addSequential(new DriveToWaypoint(w,drivetrain));
    	
    	//turns to heading
    	addSequential(new TurnToHeading(w,drivetrain));
    	
    	/**
    	 * The three lines commented out below are theoretically not necessary 
    	 * if it is assumed the lift is being approached having started out relatively perpendicular to it. 
    	 */
    	
    	//finds peg
//    	addSequential(new SetWaypointToPeg(w));
    	
    	//turns towards the peg
    	//addSequential(new TurnToWaypoint(w,drivetrain));
    	
    	//drives to the peg
//    	addSequential(new DriveToWaypoint(w,drivetrain));
    	addSequential(new AutoDrive(Robot.getPref("StandoffDistance", 44), 0, Robot.drivetrain));
    	
    	//TODO: add functionality for shooting
    }
}
