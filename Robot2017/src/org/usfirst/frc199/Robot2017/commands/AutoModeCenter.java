package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting at center of alliance wall
 * 
 * Summary:
 * The robot drives to the lift, loads gear and shoots at boiler,
 * backs out and drives away from the boiler, turns toward the 
 * center of the field and drives forward
 * 
 */
public class AutoModeCenter extends CommandGroup {
	
	/***
	 * Commands for autonomous starting at center
	 * @param alliance true for red, false for blue 
	 */
    public AutoModeCenter(boolean alliance) {
        
    	final double ROBOT_LENGTH = 36.875; //in.
    	final double ROBOT_WIDTH = 34.75; //in
    	final double DIST_TO_LIFT = Robot.getPref("WallToLift", 114.3); //in. from alliance wall to lift (approx.)
    	final double DIVIDER_DEPTH = Robot.getPref("DividerDepth", 21.5); //in. dividers protrude from the airship toward alliance wall (approx.)
    	final double AIRSHIP_DIAGONAL = Robot.getPref("AirshipDiagonal", 80.07); //in. from corner to corner of airship;
    	final double LEFT = -1;
    	final double RIGHT = 1;
    
		//Drives to lift and aligns
    	addSequential(new AutoDrive(DIST_TO_LIFT - ROBOT_LENGTH, 0, Robot.drivetrain));
    	addSequential(new AutoAlignGear());
    	
    	//Aims and shoots
    	addParallel(new VisionAssistedShoot(0,0));
    	addSequential(new AutoDelay(5));
    	
    	
    	//Backs out of dividers, giving 6 inches of extra space for the pivot
    	addSequential(new AutoDrive(0-(DIVIDER_DEPTH + 6),0, Robot.drivetrain));
    	
    	//Turns away from boiler
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,LEFT*90, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,RIGHT*90, Robot.drivetrain));
    	}
    	
    	//METHOD 1:
    	//Drives past airship
    	addSequential(new AutoDrive((AIRSHIP_DIAGONAL / 2) + 36, 0, Robot.drivetrain));
    	
    	//Turns toward center of field
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,RIGHT*90, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,LEFT*90, Robot.drivetrain));
    	}
    	
    	//Passes baseline
    	addSequential(new AutoDrive(DIVIDER_DEPTH + 24,0, Robot.drivetrain));
	
    	/*
    	//METHOD 2:
    	addSequential(new FollowTrajectory(0-((AIRSHIP_DIAGONAL / 2) + 36)), (AIRSHIP_DIAGONAL / 2) + 36), 90));
    	*/
    }
    	
    
    	
    	
    
}
