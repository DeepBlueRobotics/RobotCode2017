package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

/**
 *
 */
public class TeleopDrive extends Command {
	DrivetrainInterface driver;
	ShooterInterface turret;

	public TeleopDrive(DrivetrainInterface drive, ShooterInterface shooter) {
		this.driver = drive;
		turret = shooter;
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		driver.drive();
		if (driver.currentControl()) {
			driver.shiftGears();
		}
		
		turret.runTurretMotor(Robot.oi.manipulator.getThrottle());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drivetrain.stopDrive();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drivetrain.stopDrive();
	}
}
