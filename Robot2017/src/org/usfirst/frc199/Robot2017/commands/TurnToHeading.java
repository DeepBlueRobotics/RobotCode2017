package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc199.Robot2017.Log;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class TurnToHeading extends Command {
	WaypointAndHeading w;
	DrivetrainInterface drivetrain;

	public TurnToHeading(WaypointAndHeading w, DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.drivetrain = drivetrain;
		this.w = w;

	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetGyro();
		drivetrain.setAngleTarget(getTargetAngle());
		Log.debug(this.getClass().toString()+ ".initialize called drivetrain.setAngleTarget(" + getTargetAngle() + ")");
		

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		
		drivetrain.updateAnglePID();
		
//		if (drivetrain.currentControl()) {
//			drivetrain.shiftGears();
//		}
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
	
	public double getTargetAngle() {
		return w.angleAtWaypoint;
	}
}
