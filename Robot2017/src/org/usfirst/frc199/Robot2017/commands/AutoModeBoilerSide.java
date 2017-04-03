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
 **/
public class AutoModeBoilerSide extends CommandGroup {

	/***
	 * 
	 * @param alliance true for blue, false for red
	 */
	public AutoModeBoilerSide(boolean alliance) {
		
//		final double LENGTH_1 = Robot.getPref("Forward Travel BoilerSide", 80.66);
		// in. from front end of robot to point on field
		
//		final double LENGTH_2 = Robot.getPref("Diagonal Travel BoilerSide", 24.6);
		// in. from front of robot to lift (after pivot)
		double forward;
		if (alliance) forward = Robot.getPref("Auto Blue Boiler Forward", 121);
		else forward = Robot.getPref("Auto Red Boiler Forward", 121);
		
		double diagonal;
		if (alliance) diagonal = Robot.getPref("Auto Blue Boiler Diagonal", 15);
		else diagonal = Robot.getPref("Auto Red Boiler Diagonal", 15);
		
		final double LENGTH_1 = forward - (Robot.getPref("Robot length", 39) -
								Robot.getPref("Distance from pivot point to front of robot", 19.5));

		final double LENGTH_2 = diagonal - (Robot.getPref("Robot length", 39) -
								Robot.getPref("Distance from pivot point to front of robot", 19.5));

		/*
		 * // METHOD 2 INSTANCE VARIABLES: final double ROBOT_LENGTH = 36.875;
		 * //in. final double DIST_TO_LIFT = Robot.getPref("Wall To Lift",
		 * 114.3); //in. from alliance wall to lift (approx.) final double
		 * ROBOT_CENTER_TO_PEG =
		 * Robot.getPref("Robot center to peg (Horizontal)", 41.42); //in
		 * horizontally from front of lift to the peg final double LIFT_TO_PEG =
		 * Robot.getPref("Lift Corner to Peg (vertical) BoilerSide", 17.647);
		 * //in vertically from front of lift to the peg
		 */
		
		//uncomment this paragraph below when intake put back on
//		addSequential(new ToggleIntake(true, true, Robot.intake));
//		addSequential(new AutoDelay(0.25, Robot.intake, Robot.drivetrain));
//		addSequential(new ToggleIntake(true, true, Robot.intake));
//		addSequential(new AutoDelay(0.25, Robot.intake, Robot.drivetrain));
//		addSequential(new ToggleIntake(true, false, Robot.intake));
		addSequential(new ToggleIntakeRamp(true, true, Robot.intake));
		addSequential(new ToggleIntakeRamp(true, true, Robot.intake));

		// METHOD 1:
		// Drives to airlift
		addSequential(new AutoDrive(LENGTH_1, 0, Robot.drivetrain));

		// Turns toward lift
		if(alliance)
			addSequential(new AutoDrive(0, 60, Robot.drivetrain));
		else
			addSequential(new AutoDrive(0, -60, Robot.drivetrain));
	

		//Drives toward lift
		addSequential(new AutoDrive(LENGTH_2, 0, Robot.drivetrain));
		
		// drives up to lift and aligns, shoots
//		addSequential(new AutoAlignGear(false));

		/*
		 * //METHOD 2: addSequential(new
		 * FollowTrajectory((0-direction)*ROBOT_CENTER_TO_PEG, DIST_TO_LIFT -
		 * ROBOT_LENGTH + LIFT_TO_PEG, LEFT*60)); addSequential(new
		 * AutoAlignGear(true));
		 */
		addSequential(new DeployGear());
		addSequential(new DeployGearEnding());

	}
}
