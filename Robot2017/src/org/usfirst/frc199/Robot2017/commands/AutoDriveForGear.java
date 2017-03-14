package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class AutoDriveForGear extends Command {

	double targetDist, targetAngle;
	DrivetrainInterface drivetrain;
	boolean angleDone = false;

	public AutoDriveForGear(DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.targetDist = Robot.vision.getDistanceToGear();
		this.targetAngle = Robot.vision.getAngleToGear();
		this.drivetrain = drivetrain;
	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.resetGyro();
		drivetrain.setDistanceTarget(targetDist);
		drivetrain.setAngleTarget(targetAngle);

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		//update targets with new/current vision values
		//reset encoders/gyro w/o reseting totalError
		if(!drivetrain.angleReachedTarget()){
			targetAngle = Robot.vision.getAngleToGear();
			drivetrain.getAnglePID().setTargetNotTotError(targetAngle);
			drivetrain.resetGyro();
			drivetrain.updateAnglePID();
		} else if(drivetrain.angleReachedTarget()){
			angleDone = true;
		} else if(angleDone && !drivetrain.distanceReachedTarget()){
			targetDist = Robot.vision.getDistanceToGear();
			drivetrain.getDistancePID().setTargetNotTotError(targetDist);
			drivetrain.resetEncoder();
			drivetrain.updateDistancePID();
		}
		
//		if(!drivetrain.angleReachedTarget())
//			drivetrain.updateAnglePID();
//		else if(!drivetrain.distanceReachedTarget())
//			drivetrain.updateDistancePID();
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		//don't need to check if angle reached target bc dist will only be reached once angle is
		return drivetrain.distanceReachedTarget();
	}

	// Called once after isFinished returns true
	public void end() {
		drivetrain.stopDrive();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
