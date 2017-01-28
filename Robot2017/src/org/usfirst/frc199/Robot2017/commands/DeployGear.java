package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DeployGear extends CommandGroup {
	
    public DeployGear(double targetDist) {
        
    	SmartDashboard.putBoolean("gear has been lifted", false);
    	
    	addSequential(new AutoAlignGear());
    	addSequential(new AutoDrive(targetDist, 0, Robot.drivetrain));
    	
    	//don't be fooled, this does make the robot wait until gearLifted()
    	addSequential(new AutoDelay(0));
    	
    	SmartDashboard.putBoolean("gear has been lifted", true);
    }
}
