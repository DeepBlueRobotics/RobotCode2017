package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class VisionAssistedShoot extends CommandGroup {

    public VisionAssistedShoot(double shooterTargetDist, double shooterRunTime) {
        
    	addParallel(new AutoAdjustHood());
    	addParallel(new AutoAdjustTurret());
    	addSequential(new AutoShoot(shooterTargetDist, shooterRunTime));
    }
}
