package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * First version of a top-level command group for autonomously approaching any lift 
 * and delivering a gear with vision. Vision implementation found in AutoDriveForGear()
 * 
 * NOT FUNCTIONAL
 */
public class AutoAlignGear extends CommandGroup {

	/**
	 * @param shoot - whether to shoot or not
	 */
	public AutoAlignGear(boolean shoot) {
		addSequential(new ToggleGearIntake(true, false, Robot.intake));
		addSequential(new WriteToNT("Vision/gearRunning", true));
		addSequential(new AutoDelay(-1, Robot.intake, Robot.drivetrain));
		addSequential(new AutoDriveForGear(Robot.drivetrain));
		if (shoot) {
			addParallel(new AutoShoot(Robot.vision.getDistanceToBoiler(), 6.9, Robot.shooter));
		}
		addSequential(new AutoDelay(0, Robot.intake, Robot.drivetrain));
		addSequential(new RunShooter(Robot.shooter, 0.1));
	}
	
	public void end() {
		Robot.drivetrain.stopDrive();
		SmartDashboard.putBoolean("Vision/gearRunning", false);
	}

	protected void interrupted() {
		end();
	}

}
