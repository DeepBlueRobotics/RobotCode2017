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
    	addParallel(new RunGearRollerOut(Robot.intake));
    	addSequential(new AutoDelay(.2, Robot.intake, Robot.drivetrain));
    	addParallel(new ToggleIntake(true,true, Robot.intake));
//    	addParallel(new DriveBackwardsGearDelivery(0.6, Robot.getPref("backOutSpeed", 1), Robot.drivetrain));
    	addParallel(new AutoDrive(-12, 0, Robot.drivetrain));
  //  	addParallel(new ToggleIntake(true, true, Robot.intake));
//    	addSequential(new ToggleIntake(true, true, Robot.intake));
//    	addSequential(new AutoDelay(.5, Robot.intake, Robot.drivetrain));
    }
    
}
