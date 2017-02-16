package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoShootMethodThingThatIsCalledALot extends CommandGroup {

    public AutoShootMethodThingThatIsCalledALot() {
    	addSequential(new AutoAdjustTurret(-Robot.getPref("halfScan", 50), Robot.shooter));
    	addSequential(new AutoAdjustTurret(2 * Robot.getPref("halfScan", 50), Robot.shooter));
    }
}
