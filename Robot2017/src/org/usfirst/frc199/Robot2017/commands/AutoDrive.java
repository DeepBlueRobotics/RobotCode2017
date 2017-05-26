package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 * Autonomously drives the robot forward or backward for a set distance, 
 * or turns it to a certain target angle, implementing PID control loops. 
 * Can turn and then drive, or only turn and only drive depending on which
 * argument is zero.
 * 
 * FUNCTIONAL
 */
public class AutoDrive extends Command {

	double targetDist, targetAngle;
	DrivetrainInterface drivetrain;
	boolean angle, angleDone;

	/**
	 * 
	 * @param targetDist - the target distance to be traveled, negative if backwards
	 * @param targetAngle - the target angle to be turned, negative if to the left
	 * @param drivetrain - specific instantiation of the drivetrain to be used
	 */
	public AutoDrive(double targetDist, double targetAngle, DrivetrainInterface drivetrain) {
		requires(Robot.drivetrain);
		this.targetDist = targetDist;
		this.targetAngle = targetAngle;
		this.drivetrain = drivetrain;
		if(targetAngle == 0)
			angle = false;
		else angle = true;
		angleDone = false;
	}

	public void initialize() {
		drivetrain.resetEncoder();
		drivetrain.resetGyro();
		drivetrain.setDistanceTarget(targetDist);
		drivetrain.setAngleTarget(targetAngle);

	}

	public void execute() {
		if(angle && !angleDone) {
			angleDone = drivetrain.angleReachedTarget();
			drivetrain.updateAnglePID();
			drivetrain.resetEncoder();
		} else if(!drivetrain.distanceReachedTarget())
			drivetrain.updateDistancePID();
		
		if (drivetrain.currentControl()) {
			drivetrain.shiftGears();
		}
	}

	public boolean isFinished() {
		if(angle)
			return drivetrain.angleReachedTarget();
		else return drivetrain.distanceReachedTarget();

	}

	public void end() {
		drivetrain.stopDrive();
		drivetrain.setShifterNeutral();
	}

	protected void interrupted() {
		end();
	}
}
