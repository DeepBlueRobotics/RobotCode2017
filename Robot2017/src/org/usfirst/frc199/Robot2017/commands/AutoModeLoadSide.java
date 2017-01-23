package org.usfirst.frc199.Robot2017.commands;

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
 * the gear is taken
 **/
public class AutoModeLoadSide extends CommandGroup {

	private boolean alliance;
	
	/***
	 * Commands for autonomous starting at left
	 * @param alliance true for red, false for blue 
	 */
    public AutoModeLoadSide(boolean alliance) {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	this.alliance = alliance;
    	
 
    	final double LEFT = -1;
    	final double RIGHT = 1;
    	final double LENGTH_1 = 101.908; //in. from front end of robot to point on field
    	final double LENGTH_2 = 11; //in. from front of robot to lift (after pivot)
    	
    	//Drives to hexagon
    	addSequential(new AutoDrive(LENGTH_1, 0));
    	
    	//Turns toward lift
    	if(alliance)
    	{
    		addSequential(new AutoDrive(0,RIGHT*60));
    	}
    	else
    	{
    		addSequential(new AutoDrive(0,LEFT*60));
    	}
    	
    	//drives up to lift and aligns
    	addSequential(new AutoDrive(LENGTH_2, 0));
     	addSequential(new AutoAlignGear());
     	
     	//Waits to allow gear to be lifted
     	addSequential(new AutoDelay(5));
    	
    	
    	
    	
    }
}
