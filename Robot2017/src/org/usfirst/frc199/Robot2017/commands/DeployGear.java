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
         * use Sammy's math for this ^
         * 
         * in sequence:
         * 	flash LED
         * 	actuate up
         * */
    	addParallel(new FlashLED(Robot.intake));
//    	addParallel(new DriveBackwardsGearDelivery(0.6, Robot.getPref("backOutSpeed", 1), Robot.drivetrain));
    	addParallel(new RunGearRollerOut(Robot.intake), .5);
    	addSequential(new AutoDelay(.15, Robot.intake, Robot.drivetrain));
    	addSequential(new ToggleIntake(true, true, Robot.intake));
    	addSequential(new ToggleIntake(true, true, Robot.intake));
    	addSequential(new AutoDelay(.2, Robot.intake, Robot.drivetrain));
    	addSequential(new DriveBackwardsGearDelivery(0.6, -.7, Robot.drivetrain));
    	addSequential(new ToggleIntake(true, false, Robot.intake));
    	addSequential(new AutoDelay(.1, Robot.intake, Robot.drivetrain));
    	addSequential(new DriveBackwardsGearDelivery(0.6, .7, Robot.drivetrain));
    }
    
}
