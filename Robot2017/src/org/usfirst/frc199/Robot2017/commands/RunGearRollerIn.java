package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RunGearRollerIn extends Command {
	
	private IntakeInterface intake;
	private Timer tim;
	private double speed;
	private boolean gearInOnce;
	private boolean intakeRollerStarted;
	
    public RunGearRollerIn(double speed, IntakeInterface intake) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.intake = intake;
    	this.speed = speed;
    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	tim = new Timer();
    	gearInOnce = false;
    	intakeRollerStarted = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!intake.haveGear() && !intakeRollerStarted) {
    		intake.runRoller(speed);
    		intakeRollerStarted = true;
    	}
    	else if(!gearInOnce){
        	tim.start();
        	gearInOnce = true;
        }
    	SmartDashboard.putBoolean("runningGearRoller", true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
  /*      if(gearInOnce){
        	return !intake.getSwitch() && tim.get() > Robot.getPref("Make sure gear is in delay time", 2);
        }
        return false; */
    	return intake.haveGear();
//    	return !intake.getSwitch();
    }

    // Called once after isFinished returns true
    protected void end() {
    	intake.runRoller(0);
  
    	
    	SmartDashboard.putBoolean("runningGearRoller", false);
//    	intake.raiseIntake();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
