package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class VisionAssistedShoot extends CommandGroup {

    public VisionAssistedShoot(double shooterTargetDist, double shooterRunTime) {
        
    	addParallel(new AutoAdjustHood(Robot.shooter));
    	addParallel(new AutoAdjustTurret(0, Robot.shooter));
    	addSequential(new AutoShoot(shooterTargetDist, shooterRunTime, Robot.shooter));
    }
}
