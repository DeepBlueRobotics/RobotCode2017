package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class AutoDrive extends Command {

	double targetDist, targetAngle;
	DrivetrainInterface drivetrain;
	boolean angleFinallyReached = false;
	boolean angle = true;

	public AutoDrive(double targetDist, double targetAngle, DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.targetDist = targetDist;
		this.targetAngle = targetAngle;
		this.drivetrain = drivetrain;
		if(targetAngle == 0)
			angle = false;
	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.resetGyro();
//		drivetrain.setDistanceTarget(targetDist);
		drivetrain.unevenSetDistanceTarget(targetDist);
		drivetrain.setAngleTarget(targetAngle);

	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		//drivetrain.autoDrive();
/**		if(!drivetrain.angleReachedTarget() && !angleFinallyReached){
			drivetrain.updateAnglePID();
		}
		else if(!angleFinallyReached){
			drivetrain.resetEncoder();
			drivetrain.resetGyro();
			angleFinallyReached = true;
		}
		else if(!drivetrain.distanceReachedTarget()){
			drivetrain.updateDistancePID();
		}*/
		
		if(angle && !drivetrain.angleReachedTarget()){
			drivetrain.updateAnglePID();
		} else if(!drivetrain.unevenDistanceReachedTarget()) {
//			drivetrain.updateDistancePID();
			drivetrain.unevenUpdateDistance();
		}
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		if(angle){
			return drivetrain.angleReachedTarget();
		} else {
//			return drivetrain.distanceReachedTarget();
			return drivetrain.unevenDistanceReachedTarget();
		}
//		return drivetrain.angleReachedTarget() && drivetrain.distanceReachedTarget();

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
