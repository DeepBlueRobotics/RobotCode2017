package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoShootRoutine extends CommandGroup {

    public AutoShootRoutine() {
    	addSequential(new AutoAdjustTurret(360, Robot.shooter));
    	addSequential(new AutoAdjustTurret(Robot.vision.getAngleToBoiler(), Robot.shooter));
    	addSequential(new AutoShoot(Robot.vision.getDistanceToBoiler(), 10, Robot.shooter));
    }
}
