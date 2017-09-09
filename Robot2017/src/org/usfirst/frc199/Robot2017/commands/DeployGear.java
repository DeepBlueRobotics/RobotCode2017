package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Delivers a previously intaken gear at a lift by running gear rollers out 
 * and actuating the intake down after a moment
 * 
 * FUNCTIONAL
 */
public class DeployGear extends CommandGroup {

    public DeployGear() {
    	addParallel(new FlashLED(Robot.intake));
    	addParallel(new RunGearRollerOut(Robot.intake));
    	addSequential(new AutoDelay(.2, Robot.intake, Robot.drivetrain));
    	addParallel(new ToggleGearIntake(true,true, Robot.intake));
    	addParallel(new AutoDrive(-12, 0, Robot.drivetrain));
    }
    
}
