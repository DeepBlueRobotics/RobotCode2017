package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting on the boiler side, with back left corner of
 * robot at the inside (closest to center-field) corner of tape
 * 
 * 
 * Summary: The robot drives forward, turns 60 degrees to be parallel with the
 * desired lift, drives forward to the lift, auto-aligns, and shoots at the
 * boiler while the gear is taken
 * 
 * FUNCTIONAL
 **/
public class AutoModeBoilerSide extends CommandGroup {

	/***
	 * @param blueAlliance true for blue, false for red
	 */
	public AutoModeBoilerSide(boolean blueAlliance) {
		double forward;
		if (blueAlliance) {
			forward = Robot.getPref("Auto Blue Boiler Forward", 121);
		} else {
			forward = Robot.getPref("Auto Red Boiler Forward", 121);
		}
		
		double diagonal;
		if (blueAlliance) {
			diagonal = Robot.getPref("Auto Blue Boiler Diagonal", 15);
		} else {
			diagonal = Robot.getPref("Auto Red Boiler Diagonal", 15);
		}
		
		final double LENGTH_1 = forward - (Robot.getPref("Robot length", 39) -
								Robot.getPref("Distance from pivot point to front of robot", 19.5));

		final double LENGTH_2 = diagonal - (Robot.getPref("Robot length", 39) -
								Robot.getPref("Distance from pivot point to front of robot", 19.5));

		// METHOD 1:
		// Drives to airlift
		addParallel(new ShiftToLowGear(Robot.drivetrain));
		addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));

		// Turns toward lift
		if(blueAlliance) {
			addSequential(new AutoDrive(0, 60, Robot.drivetrain));
		} else {
			addSequential(new AutoDrive(0, -60, Robot.drivetrain));
		}
	

		//Drives toward lift
		addSequential(new AutoDrive(LENGTH_2, 0, Robot.drivetrain));
		
		// drives up to lift and aligns, shoots
//		addSequential(new AutoAlignGear(false));
		
		addSequential(new DeployGear());
		addSequential(new DeployGearEnding());

	}
}
