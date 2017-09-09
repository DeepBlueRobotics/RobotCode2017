package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DeployGearEnding extends CommandGroup {

    public DeployGearEnding() {
    	addSequential(new ToggleGearIntake(true, false, Robot.intake));
    	addSequential(new AutoDelay(5, Robot.intake, Robot.drivetrain));
//    	addSequential(new AutoDrive(12, 0, Robot.drivetrain));
    }
}
