package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting on the boiler side,
 * with back left corner of robot at the inside (closest to center-field) 
 * corner of tape
 * 
 * 
 * Summary:
 * The robot drives forward, turns 60 degrees to be parallel with the
 * desired lift, drives forward to the lift, auto-aligns, and shoots
 * at the boiler while the gear is taken
 **/
public class AutoModeBoilerSide extends CommandGroup {

	/***
	 * Commands for autonomous starting at left
	 * @param alliance true for red, false for blue 
	 */
    public AutoModeBoilerSide(boolean alliance) {
        
    	final double LENGTH_1 = Robot.getPref("Forward Travel BoilerSide", 95.742);; //in. from front end of robot to point on field
    	/*
    	// METHOD 2 INSTANCE VARIABLES:
    	final double LEFT = -1;
    	final double RIGHT = 1;
    	final double ROBOT_LENGTH = 36.875; //in.
    	final double DIST_TO_LIFT = Robot.getPref("Wall To Lift", 114.3); //in. from alliance wall to lift (approx.)
    	final double ROBOT_CENTER_TO_PEG = Robot.getPref("Robot center to peg (Horizontal)", 41.42); //in horizontally from front of lift to the peg
    	final double LIFT_TO_PEG = Robot.getPref("Lift Corner to Peg (vertical) BoilerSide", 17.647); //in vertically from front of lift to the peg
    	*/
    	
    	//METHOD 1:
    	//Drives to airlift
    	addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));
    	
    	//Turns toward lift
    	addSequential(new AutoDrive(0,Robot.vision.getAngleToGear(), Robot.drivetrain));
    	
    	//drives up to lift and aligns
    	addSequential(new AutoDrive(Robot.vision.getDistanceToGear(), 0, Robot.drivetrain));
     	addSequential(new AutoAlignGear());
     	
     	/*
     	//METHOD 2:
     	if(alliance)
    	{
    		addSequential(new FollowTrajectory(LEFT*ROBOT_CENTER_TO_PEG, DIST_TO_LIFT - ROBOT_LENGTH + LIFT_TO_PEG, LEFT*60));
    	}
    	else
    	{
    		addSequential(new FollowTrajectory(RIGHT*ROBOT_CENTER_TO_PEG, DIST_TO_LIFT - ROBOT_LENGTH + LIFT_TO_PEG, RIGHT*60));
    	}
     	 
     	addSequential(new AutoAlignGear());
     	*/
     	
     	
     	//Aims and shoots
     	addParallel(new AutoShoot(Robot.vision.getDistanceToBoiler(),10, Robot.shooter));
    	addSequential(new AutoDelay(0, Robot.intake));
    	
    
    	
    }
}
