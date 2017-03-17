package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAlignGear extends CommandGroup {

	/**
	 * @param shoot - whether to shoot or not
	 */
	public AutoAlignGear(boolean shoot) {
		addSequential(new ToggleIntake(true, false, Robot.intake));
		addSequential(new WriteToNT("Vision/gearRunning", true));
		addSequential(new AutoDelay(-1, Robot.intake));
		addSequential(new AutoDelay(0.25, Robot.intake));
//		addSequential(new AutoDrive(Robot.vision.getDistanceToGear(), Robot.vision.getAngleToGear(), Robot.drivetrain));
//		addSequential(new AutoDrive(0, Robot.vision.getAngleToGear(), Robot.drivetrain));
//		addSequential(new AutoDrive(Robot.vision.getDistanceToGear(), 0, Robot.drivetrain));
		addSequential(new AutoDriveForGear(Robot.drivetrain));
		if (shoot) {
			addParallel(new AutoShoot(Robot.vision.getDistanceToBoiler(), 6.9, Robot.shooter));
		}
		addSequential(new AutoDelay(0, Robot.intake));
		addSequential(new RunShooter(0, Robot.shooter, 0.1));

		addSequential(new WriteToNT("Vision/gearRunning", false));
	}
	
	public void end() {
		Robot.drivetrain.stopDrive();
		SmartDashboard.putBoolean("Vision/gearRunning", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}
