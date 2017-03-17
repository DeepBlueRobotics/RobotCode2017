package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class NeverEndTheIntaking extends Command {

	Timer tim;
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
		intaking = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//This toggle the intake once, and after 1 sec, stops the toggle motor and starts intaking

		if(tim.get() <= 1) {
			intake.toggleIntake(true, true);
		} else {
			if(!intaking) {
				intaking = true;
				intake.stopIntake();
			} else {
				intake.controlledIntake(false);
			}
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return tim.get() > 1 && intake.intakeIsDown();
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
