package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc199.Robot2017.Robot;

/**
 *
 */
public class AutoModeBasic extends CommandGroup {

	public AutoModeBasic() {
		addSequential(new AutoDrive(75, 0, Robot.drivetrain));
	}

}