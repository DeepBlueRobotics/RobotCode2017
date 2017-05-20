package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Delays an autonomous process either for a specified amount of time, 
 * until the gear has been lifted from the intake, or 
 * until a vision target has been found
 * 
 * If delaying for vision, command will turn drivetrain to aid with finding target
 * 
 * FUNCTIONAL
 */
public class AutoDelay extends Command {

	Timer tim = new Timer();
	private double time;
	IntakeInterface intake;
	DrivetrainInterface drivetrain;
	boolean angleDone = false;

	/**
	 * @param time - the amount of time in seconds that you want the command to delay for
	 * @param intake - our intake system
	 * @param drivetrain - our drivetrain system
	 */
	public AutoDelay(double time, IntakeInterface intake, DrivetrainInterface drivetrain) {
		this.time = time;
		this.intake = intake;
		this.drivetrain = drivetrain;
	}

	protected void initialize() {

		tim.start();
		tim.reset();
	}

	protected void execute() {
	}

	/** 
	 * @return if the specified time parameter is -1, return true if gear marks have been found
	 * @return if the specified time parameter is 0, return true if the peg has passed through 
	 * 		the gear mechanism, and we are running a vision process
	 * @return else return true after the specified amount of time has passed
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

	protected void end() {
		if (time == -1) {
			drivetrain.stopDrive();
		}
	}

	protected void interrupted() {
		end();
	}
}
