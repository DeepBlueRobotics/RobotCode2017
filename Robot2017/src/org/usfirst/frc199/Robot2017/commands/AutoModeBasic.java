package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc199.Robot2017.Robot;

/**
 * Basic auto mode used to cross baseline
 * 
 * FUNCTIONAL
 * 
 */
public class AutoModeBasic extends CommandGroup {

	public AutoModeBasic() {
		addSequential(new AutoDrive(75, 0, Robot.drivetrain));
	}

}