package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting on the load side, with center of
 * robot at the inside (closest to center-field) corner of tape
 * 
 * 
 * Summary: The robot drives forward, turns 60 degrees to be parallel with the
 * desired lift, drives forward to the lift, and then the gear is taken (5
 * seconds allotted)
 * 
 * Next, it reverses the path it took to the boiler, turns away from the boiler
 * and backs up towards it, then shoots
 * 
 * FUNCTIONAL
 **/
public class AutoModeLoadSide extends CommandGroup {

	/***
	 * @param alliance true for blue, false for red
	 */
	public AutoModeLoadSide(boolean alliance) {
		
		double forward;
		if (alliance) {
			forward = Robot.getPref("Auto Blue Load Forward", 110);
		} else {
			forward = Robot.getPref("Auto Red Load Forward", 116);
		}
		
		double diagonal;
		if (alliance) {
			diagonal = Robot.getPref("Auto Blue Load Diagonal", 50);
		} else {
			diagonal = Robot.getPref("Auto Red Load Diagonal", 50);
		}
		
		final double LENGTH_1 = forward - (Robot.getPref("Robot length", 39) -
				Robot.getPref("Distance from pivot point to front of robot", 19.5));

		final double LENGTH_2 = diagonal - (Robot.getPref("Robot length", 39) -
				Robot.getPref("Distance from pivot point to front of robot", 19.5));
		
		// METHOD 1
		// Drives to hexagon
		addParallel(new ShiftToLowGear(Robot.drivetrain));
		addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));

		// Turns toward lift
		if(alliance) {
			addSequential(new AutoDrive(0, -60, Robot.drivetrain));
		} else {
			addSequential(new AutoDrive(0, 60, Robot.drivetrain));
		}
		
		// Drive Forward
		addSequential(new AutoDrive(LENGTH_2, 0, Robot.drivetrain));
		
		// drives up to lift and aligns
		
		addSequential(new DeployGear());
		addSequential(new DeployGearEnding());

	}
}
