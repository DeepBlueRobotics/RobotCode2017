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
 * FUNCTIONAL
 */
public class AutoModeCenter extends CommandGroup {

	/**
	 * @param alliance - true for red, false for blue
	 */
		//Big boink theory
	public AutoModeCenter(boolean alliance) {		

		addSequential(new AutoDrive(78, 0, Robot.drivetrain));
		
		addSequential(new DeployGear());
		addSequential(new DeployGearEnding());

	}

}
