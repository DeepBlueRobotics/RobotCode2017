package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting on the load side,
 * with back left corner of robot at the inside (closest to center-field) 
 * corner of tape
 * 
 * 
 * Summary:
 * The robot drives forward, turns 60 degrees to be parallel with the
 * desired lift, drives forward to the lift, auto-aligns, and then
 * the gear is taken (5 seconds allotted)
 * 
 * Next, it reverses the path it took to the boiler, turns away from
 * the boiler and backs up towards it, then shoots
 * 
 * 
 **/
public class AutoModeLoadSide extends CommandGroup {
	
	/***
	 * Commands for autonomous starting at left
	 * @param alliance true for red, false for blue 
	 */
    public AutoModeLoadSide(boolean alliance) {
        
    	final double LEFT = -1;
    	final double RIGHT = 1;
    	final double LENGTH_1 = Robot.getPref("Forward Travel LoadSide", 101.908); //in. from front end of robot to point on field
    	final double LENGTH_2 = Robot.getPref("Diagonal Travel LoadSide", 11); //in. from front of robot to lift (after pivot)
    	final double DIST_TO_LIFT = Robot.getPref("Wall To Lift", 114.3); //in. from alliance wall to lift (approx.)
    	final double LIFT_TO_PEG_X = Robot.getPref("Lift Corner to Peg (Horizontal) LoadSide", 30.739); //in horizontally from front of lift to the peg
    	final double LIFT_TO_PEG_Y = Robot.getPref("Lift Corner to Peg (Vertical) LoadSide", 17.647); //in vertically from front of lift to the peg
    	
    	//METHOD 1
    	//Drives to hexagon
    	addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));
    	
    	//Turns toward lift
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,RIGHT*60, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,LEFT*60, Robot.drivetrain));
    	}
    	
    	//drives up to lift and aligns
    	addSequential(new AutoDrive(LENGTH_2, 0, Robot.drivetrain));
     	addSequential(new AutoAlignGear());
     	
     	/*
     	//METHOD 2:
     	addSequential(new FollowTrajectory(LIFT_TO_PEG_X, DIST_TO_LIFT - ROBOT_LENGTH + LIFT_TO_PEG_Y, 60));
     	addSequential(new AutoAlignGear());
     	*/
     	
     	//Waits to allow gear to be lifted
     	addSequential(new AutoDelay(5, Robot.intake));
    	
     	
     	//METHOD 1:
     	//backs up
     	addSequential(new AutoDrive(0-LENGTH_2, 0, Robot.drivetrain));
     	
     	//Turns away from lift
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,LEFT*60, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,RIGHT*60, Robot.drivetrain));
    	}
    	
    	//backs up
    	addSequential(new AutoDrive(6-LENGTH_1, 0, Robot.drivetrain));
    	
    	//turns away from boiler and drives backward
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,LEFT*90, Robot.drivetrain));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,RIGHT*90, Robot.drivetrain));
    	}
    	addSequential(new AutoDrive(-120, 0, Robot.drivetrain));
    	
    	//TODO: Method 2
    	
    	//Aims and shoots
    	addParallel(new VisionAssistedShoot(0,0));
   
    	
    }
}
