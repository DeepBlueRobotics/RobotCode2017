package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToggleIntakeRamp extends Command {
	IntakeInterface intake;
	boolean firstTime = true;
	Timer tim = new Timer();
	boolean giveDirection;
	boolean extend;

	public ToggleIntakeRamp(boolean giveDirection, boolean extend, IntakeInterface intake) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.intake = intake;
		requires(Robot.intake);
		this.giveDirection = giveDirection;
		this.extend = extend;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.start();
		firstTime = true;
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
//		if (firstTime) {
//			intake.toggleFlipperFlapper();
//			firstTime = false;
//		}
		if (firstTime) {
			if(giveDirection) {
				if(extend) intake.lowerFlipperFlapper();
				else intake.raiseFlipperFlapper();
			} else {
				intake.toggleFlipperFlapper();
			}
			firstTime = !firstTime;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return tim.get() >= 0.0625;
	}

	// Called once after isFinished returns true
	public void end() {
		intake.setFlipperFlapperNeutral();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
