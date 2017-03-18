package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class AutoDriveForGear extends Command {

	DrivetrainInterface drivetrain;
	boolean angleDone = false;
	Timer tim = new Timer();
	double noResetPeriod = Robot.getPref("AlignGear vision update period", 0.02);
	double targetDist, targetAngle;
	boolean stopAndRecheck, commandCanBeDone;

	public AutoDriveForGear(DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.drivetrain = drivetrain;
	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.resetGyro();
		targetDist = Robot.vision.getDistanceToGear();
		targetAngle = Robot.vision.getAngleToGear();
		
		if(targetDist > 24){
			targetDist = targetDist - 24;
			stopAndRecheck = true;
			commandCanBeDone = false;
		} else {
			stopAndRecheck = false;
			commandCanBeDone = true;
		}
		
		drivetrain.setDistanceTarget(targetDist);
		drivetrain.setAngleTarget(targetAngle);
		tim.start();
		tim.reset();
		angleDone = false;
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		if(!angleDone){
			angleDone = drivetrain.angleReachedTarget();
			drivetrain.updateAnglePID();
			drivetrain.resetEncoder();
//		} else if(!drivetrain.distanceReachedTarget()){
		} else {
			drivetrain.updateDistancePID();
		}
		
		if(stopAndRecheck && drivetrain.distanceReachedTarget()){
			angleDone = false;
			stopAndRecheck = false;
			// I DO reset targetAngle here even tho does reset in if statement below just in case
			//that if statement isn't entered bc vision = SLOW
			drivetrain.getAnglePID().setTargetNotTotError(Robot.vision.getAngleToGear());
			drivetrain.getDistancePID().setTargetNotTotError(Robot.vision.getDistanceToGear());
			drivetrain.resetEncoder();
			drivetrain.resetGyro();
			commandCanBeDone = true;
		}
		
		if(tim.get() > noResetPeriod) {
//			if(targetDist != Robot.vision.getDistanceToGear()){
//				targetDist = Robot.vision.getDistanceToGear();
//				drivetrain.getDistancePID().setTargetNotTotError(Robot.vision.getDistanceToGear());
//				drivetrain.resetEncoder();
//			}
			if(targetAngle != Robot.vision.getAngleToGear()){
				targetAngle = Robot.vision.getAngleToGear();
				drivetrain.getAnglePID().setTargetNotTotError(Robot.vision.getAngleToGear());
				drivetrain.resetGyro();
			}
			tim.reset();
		}
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		//don't need to check if angle reached target bc dist will only be reached once angle is
		return commandCanBeDone && drivetrain.distanceReachedTarget();
	}

	// Called once after isFinished returns true
	public void end() {
		drivetrain.stopDrive();
		tim.reset();
		tim.stop();
		SmartDashboard.putBoolean("Vision/gearRunning", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
