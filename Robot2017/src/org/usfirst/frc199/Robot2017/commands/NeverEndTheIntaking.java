package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class NeverEndTheIntaking extends Command {

	Timer tim;
	boolean beenHereB4 = false;
	boolean intaking = false;
	IntakeInterface intake;

	public NeverEndTheIntaking(IntakeInterface intake) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.intake = intake;
		tim = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.reset();
		tim.start();
		beenHereB4 = false;
		intaking = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//This toggle the intake once, and after 1 sec, stops the toggle motor and starts intaking
		if (!beenHereB4) {
			beenHereB4 = true;
			intake.toggleIntake(true, true);
		}
		if (tim.get() > 1) {
			if (intaking) {
				intake.controlledIntake(false);
			} else {
				intaking = true;
				intake.setIntakePistonNeutral();
			}
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
