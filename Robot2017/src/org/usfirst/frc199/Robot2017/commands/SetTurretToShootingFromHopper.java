package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetTurretToShootingFromHopper extends Command {
	double angleToBoiler;
	ShooterInterface shooter;
    public SetTurretToShootingFromHopper(ShooterInterface shooter) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.shooter = shooter;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	angleToBoiler = Robot.getPref("AngleToBoilerInDegrees", 10)*Robot.getPref("Alliance", 1);
    	shooter.resetTurretEncoder();
    	shooter.setTurretPIDTarget(angleToBoiler);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	shooter.setTurretPIDTarget(angleToBoiler - shooter.getTurretEncoder());
    	shooter.runTurretMotor(shooter.getTurretPIDOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return shooter.turretPIDTargetReached();
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooter.resetTurretEncoder();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
