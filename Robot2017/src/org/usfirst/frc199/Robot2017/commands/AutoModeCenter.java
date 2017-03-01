package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is to be used if starting at center of alliance wall
 * 
 * Summary: The robot drives to the lift, loads gear and shoots at boiler, backs
 * out and drives away from the boiler, turns toward the center of the field and
 * drives forward
 * 
 */
public class AutoModeCenter extends CommandGroup {

	/***
	 * Commands for autonomous starting at center
	 * 
	 * @param alliance true for red, false for blue
	 */
	public AutoModeCenter(boolean alliance) {

		double direction = 1;
		if (alliance)
			direction = -1;

		final double DIVIDER_DEPTH = Robot.getPref("DividerDepth", 21.5); // in.
																			// dividers
																			// protrude
																			// from
																			// the
																			// airship
																			// toward
																			// alliance
																			// wall
																			// (approx.)
		final double AIRSHIP_DIAGONAL = Robot.getPref("AirshipDiagonal", 80.07); // in.
																					// from
																					// corner
																					// to
																					// corner
																					// of
																					// airship;

		// Drives to lift and aligns
		addSequential(new AutoAlignGear(true));

		// Backs out of dividers, giving 6 inches of extra space for the pivot
		addSequential(new AutoDrive(0 - (DIVIDER_DEPTH + 6), 0, Robot.drivetrain));

		// Turns away from boiler
		addSequential(new AutoDrive(0, (0 - direction) * 90, Robot.drivetrain));

		// METHOD 1:
		// Drives past airship
		addSequential(new AutoDrive((AIRSHIP_DIAGONAL / 2) + 36, 0, Robot.drivetrain));

		// Turns toward center of field

		addSequential(new AutoDrive(0, direction * 90, Robot.drivetrain));

		// Passes baseline
		addSequential(new AutoDrive(DIVIDER_DEPTH + 24, 0, Robot.drivetrain));

		/*
		 * //METHOD 2: addSequential(new FollowTrajectory((AIRSHIP_DIAGONAL / 2)
		 * + 36, (AIRSHIP_DIAGONAL / 2) + 36, direction*90));
		 */

	}

}
