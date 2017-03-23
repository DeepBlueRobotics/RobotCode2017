package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc199.Robot2017.Log;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class TurnToHeading extends Command {

	double targetAngle;
	DrivetrainInterface drivetrain;

	public TurnToHeading(WaypointAndHeading w, DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.targetAngle = w.newHeadingAtWaypoint;
		this.drivetrain = drivetrain;
		
	}

	// Called just before this Command runs the first time
	public void initialize() {
		
		drivetrain.setAngleTarget(targetAngle);
		Log.debug(this.getClass().toString()+ ".initialize called drivetrain.setAngleTarget(" + targetAngle + ")");
		

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		
		drivetrain.updateAnglePID();
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		
		return drivetrain.angleReachedTarget();

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
