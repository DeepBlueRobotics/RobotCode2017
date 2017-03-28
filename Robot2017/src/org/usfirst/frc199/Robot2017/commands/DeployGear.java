package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DeployGear extends CommandGroup {

    public DeployGear() {
    	/**
         * should do the following
         * 
         * in parallel:
         * 	run roller in reverse
         * 	actuate down
         * 	drive backwards
         * use Sammi's math for this ^
         * 
         * in sequence:
         * 	flash LED
         * 	actuate up
         * */
    	addSequential(new ToggleIntake(true, true, Robot.intake));
    	addParallel(new RunGearRollerOut(Robot.intake));
    	addParallel(new DriveBackwardsGearDelivery(0.6, Robot.getPref("backOutSpeed", 2), Robot.drivetrain));
    	addSequential(new FlashLED(Robot.intake));
    }
}
