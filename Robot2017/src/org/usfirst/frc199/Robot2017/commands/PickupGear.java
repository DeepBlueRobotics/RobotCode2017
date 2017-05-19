package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PickupGear extends CommandGroup {

    public PickupGear() {
        /**
         * should do the following:
         * 
         * in parallel:
         * 	actuate gear intake down
         * 	spin gear roller
         * 	when limit switch actuated
         * 		flash LED
         * 	if limit switch still actuated for long enough
         * 		stop roller, set to brake
         * 		actuate up
         * */
    	addParallel(new FlashLED(Robot.intake));
    	if (Robot.drivetrain.inHighGear()) {
        	addSequential(new ToggleDrivetrainShift(Robot.drivetrain));
    	}
    	addSequential(new ToggleIntake(true, true, Robot.intake));
    	addSequential(new ToggleIntake(true, true, Robot.intake));
    	addSequential(new RunGearRollerIn(.9, Robot.intake));
    	addSequential(new ToggleDrivetrainShift(Robot.drivetrain));
    	addSequential(new ToggleIntake(true, false, Robot.intake));
    }
    
//    protected boolean isFinished() {
//    	return !Robot.intake.getSwitch();
//    }
}
