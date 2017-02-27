package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAdjustTurret extends Command {
	
	private ShooterInterface shooter;
	private double target;
	private double convertedTarget;
	
    public AutoAdjustTurret(double target, ShooterInterface shooter) {
    	this.shooter = shooter;
    	this.target = target;
    	requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	convertedTarget = shooter.convertAngleToTargetDistance(target);
    	shooter.setTurretPIDTarget(convertedTarget);
    	shooter.resetTurretEncoder();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	shooter.updateTurretPID(shooter.getTurretEncoder());
    	shooter.runTurretMotor(shooter.getTurretPIDOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(target == 360){
    		return SmartDashboard.getBoolean("Vision/boilerFound", false);
    	}
        return shooter.turretPIDTargetReached();
    }

    // Called once after isFinished returns true
    protected void end() {
    	shooter.stopTurretMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
