package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDelay extends Command {

	Timer tim = new Timer();
	private double time;
	IntakeInterface intake;
	DrivetrainInterface drivetrain;
	boolean angleDone = false;

	/**
	 * can use either time limit or the end condition of gear being lifted for
	 * this command
	 * if using time limit, just enter the time
	 * if using end condition, enter 0 for time
	 * 
	 * @param  time   the amount of time in seconds that you want the command to delay for
	 * @param  intake simply our intake system
	 */
	public AutoDelay(double time, IntakeInterface intake, DrivetrainInterface drivetrain) {
		this.time = time;
		this.intake = intake;
		this.drivetrain = drivetrain;
	}

	/**
	 * Called just before this Command runs the first time to start the timer and the
	 * drivetrain things if time is set to -1
	 */
	protected void initialize() {
		if (time == -1) {
			drivetrain.resetEncoder();
			drivetrain.resetGyro();
			drivetrain.setDistanceTarget(-18);
			drivetrain.setAngleTarget(-15);
		}

		tim.start();
		tim.reset();
	}

	/**
	 * Called repeatedly when this Command is scheduled to run
	 */
	protected void execute() {
		if (time == -1 && tim.get() > 3 && !SmartDashboard.getBoolean("Vision/OH-YEAH", false)) {
			if (!angleDone) {
				angleDone = drivetrain.angleReachedTarget();
				drivetrain.updateAnglePID();
				drivetrain.resetEncoder();
			} else {
				drivetrain.updateDistancePID();
			}
		}
	}

	/**
	 * Make this return true when this Command no longer needs to run execute()
	 * 
	 * @return no idea ask Ana
	 */
	protected boolean isFinished() {
		if (time == -1) {
			return SmartDashboard.getBoolean("Vision/OH-YEAH", false);
		} else if (time == 0) {
			return intake.gearLifted(true) && SmartDashboard.getBoolean("Vision/OH-YEAH", false);
		} else {
			return tim.get() >= time;
		}
	}

	/**
	 * Called once after isFinished returns true.
	 *
	 * Currently does nothing.
	 */
	protected void end() {
	}

	/**
	 * Called when another command which requires one or more of the same
	 * subsystems is scheduled to run. 
	 *
	 * Currently does nothing.
	 */
	protected void interrupted() {
	}
}
