package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class DriveToWaypoint extends Command {

	double targetDist;
	DrivetrainInterface drivetrain;

	public DriveToWaypoint(WaypointAndHeading w, DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.targetDist = w.distanceToWaypoint;
		this.drivetrain = drivetrain;
	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.setDistanceTarget(targetDist);

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		drivetrain.updateDistancePID();
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		return drivetrain.distanceReachedTarget();

	}

	// Called once after isFinished returns true
	public void end() {
		drivetrain.stopDrive();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
