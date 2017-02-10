package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAlignGear extends CommandGroup {

    public AutoAlignGear() {
        while(SmartDashboard.getBoolean("Vision/gearVisionRunning", false)) {
        	addSequential(new AutoDelay(-1, Robot.intake));
        }
        addSequential(new AutoDelay(0.25, Robot.intake));
        if (SmartDashboard.getBoolean("Vision/OH-YEAH", false)) {
        	addSequential(new AutoDrive(Robot.vision.getDistanceToGear(), Robot.vision.getAngleToGear(), Robot.drivetrain));
        	addSequential(new AutoDelay(0, Robot.intake));
        }
    }

}
