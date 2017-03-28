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
    	addParallel(new FlashLED(Robot.intake));
    }
}
