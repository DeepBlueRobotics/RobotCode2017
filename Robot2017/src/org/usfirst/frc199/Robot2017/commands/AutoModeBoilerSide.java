package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

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
	public AutoModeBoilerSide(boolean blueAlliance, DrivetrainInterface drivetrain) {
		
		double l = Robot.getPref("Robot length", 36.375);
		double x = Robot.getPref("Horz dist to pivot pt from left", 17.41915241);
		double h = Robot.getPref("Vert dist to pivot pt from back", 11.15142947);
		double b = Robot.getPref("Auto buffer", 1);
		double theta = Math.toDegrees(Math.atan(x/h));
		double hPlusxSquared = Math.pow(h, 2) + Math.pow(x, 2);
		double d1;
		double d2;
		double df;
		double dt;
		
		if(!blueAlliance){
			d1 = Robot.getPref("Auto Red Boiler Forward", 110);
			d2 = Robot.getPref("Auto Red Boiler Diagonal", 50);
			df = d1 - l - h;
			theta = -theta;
			dt = d2 + hPlusxSquared - b;
		} else {
			d1 = Robot.getPref("Auto Blue Boiler Forward", 110);
			d2 = Robot.getPref("Auto Blue Boiler Diagonal", 50);
			df = d1 - l + h;
			dt = d2 - hPlusxSquared - b;
		}
		
		
		
		// METHOD 1
		
		//Shift to low gear
		addParallel(new ShiftToLowGear(drivetrain));
		// Drives to hexagon
		addSequential(new AutoDrive(df, 0, drivetrain));

		// Turns toward lift
		addSequential(new AutoDrive(0, theta, drivetrain));

		// Drive Forward
		addSequential(new AutoDrive(dt, 0, drivetrain));
		
		// drives up to lift and aligns
		addSequential(new DeployGear());
		addSequential(new DeployGearEnding());
	}
}
