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
        
    	final double ROBOT_LENGTH = 36.875; //in.
    	final double ROBOT_WIDTH = 34.75; //in
    	final double LEFT = -1;
    	final double RIGHT = 1;
    	final double LENGTH_1 = Robot.getPref("ForwardTravelLoadSide", 95.742);; //in. from front end of robot to point on field
    	final double LENGTH_2 = Robot.getPref("DiagonalTravelLoadSide", 23.5); //in. from front of robot to lift (after pivot)
    	final double DIST_TO_LIFT = 114.3; //in. from alliance wall to lift (approx.)
    	final double LIFT_TO_PEG_X = 41.42; //in horizontally from front of lift to the peg
    	final double LIFT_TO_PEG_Y = 17.647; //in vertically from front of lift to the peg
    	
    	
    	//METHOD 1:
    	//Drives to airlift
    	addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));
    	
    	//Turns toward lift
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,LEFT*60, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,RIGHT*60, Robot.drivetrain));
    	}
    	
    	//drives up to lift and aligns
    	addSequential(new AutoDrive(LENGTH_2, 0, Robot.drivetrain));
     	addSequential(new AutoAlignGear());
     	
     	/*
     	//METHOD 2:
     	addSequential(new FollowTrajectory(0-LIFT_TO_PEG_X, DIST_TO_LIFT - ROBOT_LENGTH + LIFT_TO_PEG_Y, -60));
     	addSequential(new AutoAlignGear());
     	*/
     	
     	//Aims and shoots
    	addParallel(new VisionAssistedShoot(0,0));
    	addSequential(new AutoDelay(5, Robot.intake));
    	
    	//TODO: Dispense boiler
    	
    }
}
