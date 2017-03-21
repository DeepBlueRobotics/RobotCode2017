package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 * Drives the robot towards the gear in autonomous. While it is looking for gear, turns 
 * right and goes back until found.
 */
public class AutoDriveForGear extends Command {

	DrivetrainInterface drivetrain;
	boolean angleDone = false;
	Timer tim = new Timer();
	double noResetPeriod = Robot.getPref("AlignGear vision update period", 0.02);
	double targetDist, targetAngle;
	
	// Conditionals for rechecking alignment
	boolean stopAndRecheck, commandCanBeDone;
	
	// Conditionals for realigning horizontally from a bird's eye view
	boolean turn1Done, realignDistanceDone, turn2Done, realign;
	
	public AutoDriveForGear(DrivetrainInterface drivetrain){
		requires(Robot.drivetrain);
		this.drivetrain = drivetrain;
	}

	// Called just before this Command runs the first time
	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.resetGyro();
		
		targetDist = Robot.vision.getDistanceToGear();
		
		drivetrain.setAngleTarget(Robot.vision.getAngleToGear());
		
		if(targetDist > 30){
			targetDist = targetDist - 29;
			stopAndRecheck = true;
			commandCanBeDone = false;
		} else {
			stopAndRecheck = false;
			commandCanBeDone = true;
		}
		
		drivetrain.setDistanceTarget(targetDist);
		
		turn1Done = false; realignDistanceDone = false; turn2Done = false; realign = false;
		tim.start();
		tim.reset();
		angleDone = false;
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		
		realign = angleDone && Robot.vision.alignedButHorizontallyOffset();
		
		// update targets with new/current vision values
		// reset encoders/gyro w/o reseting totalError
		if (!realign) {
			if(!angleDone){
				angleDone = drivetrain.angleReachedTarget();
				drivetrain.updateAnglePID();
				drivetrain.resetEncoder();
			} else {
				resetTarget(Robot.vision.getDistanceToGear(), 0);
				drivetrain.updateDistancePID();
//				if (drivetrain.distanceReachedTarget()) SmartDashboard.putBoolean("Vision/OH-YEAH", false);
			}
		} else if (realign){
			if(!turn1Done) {
				resetTarget(0, Robot.vision.angleToTurnIfHorizontallyOffset());
				drivetrain.updateAnglePID();
				turn1Done = drivetrain.angleReachedTarget();
			} else if (!realignDistanceDone) {
				resetTarget(Robot.vision.distanceToTravelIfHorizontallyOffset(), 0);
				drivetrain.updateDistancePID();
				realignDistanceDone = drivetrain.distanceReachedTarget();
			} else if(!turn2Done) {
				resetTarget(0, Robot.vision.angleToTurnBackIfHorizontallyOffset());
				drivetrain.updateAnglePID();
				turn2Done = drivetrain.angleReachedTarget();
				commandCanBeDone = turn2Done;
				realign = !turn2Done;
			} 
		}
		
		if(stopAndRecheck && SmartDashboard.getBoolean("Vision/OH-YEAH", false) && drivetrain.distanceReachedTarget()){
			resetTarget(Robot.vision.getDistanceToGear(), Robot.vision.getAngleToGear());
			angleDone = false;
			stopAndRecheck = false;
			commandCanBeDone = true;
		}
		
		if(tim.get() > noResetPeriod) {
//				if(targetDist != Robot.vision.getDistanceToGear()){
//					drivetrain.getDistancePID().setTargetNotTotError(Robot.vision.getDistanceToGear());
//					drivetrain.resetEncoder();
//				}
//			if(targetAngle != Robot.vision.getAngleToGear()){
//				drivetrain.getAnglePID().setTargetNotTotError(Robot.vision.getAngleToGear());
//				drivetrain.resetGyro();
//			}
			tim.reset();
		}
		
		if(drivetrain.currentControl()){
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
	
	private void resetTarget(double dist, double angle) {
		if(drivetrain.distanceReachedTarget()) {
			commandCanBeDone = false;
			drivetrain.resetEncoder();
			drivetrain.setDistanceTarget(dist);
			if(angle == 0) drivetrain.setAngleTarget(0);
		}
		if(drivetrain.angleReachedTarget()) {
			commandCanBeDone = false;
			drivetrain.resetGyro();
			drivetrain.setAngleTarget(angle);
			if(dist == 0) drivetrain.setDistanceTarget(0);
		}
	}
}
