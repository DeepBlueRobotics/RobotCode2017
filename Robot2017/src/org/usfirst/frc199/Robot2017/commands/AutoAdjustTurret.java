package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * In theory, this command moves the turret to a certain set position 
 * and stops when it has reached the target angle
 * 
 * NOT TESTED
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

	protected void initialize() {
		convertedTarget = shooter.convertAngleToTargetDistance(target);
		shooter.setTurretPIDTarget(convertedTarget);
		shooter.resetTurretEncoder();
	}
	
	protected void execute() {
		shooter.updateTurretPID(shooter.getTurretEncoder());
		shooter.runTurretMotor(shooter.getTurretPIDOutput());
	}

	protected boolean isFinished() {
		if (target == 360) {
			return SmartDashboard.getBoolean("Vision/boilerFound", false);
		}
		return shooter.turretPIDTargetReached();
	}

	protected void end() {
		shooter.stopTurretMotor();
	}

	protected void interrupted() {
		end();
	}
}
