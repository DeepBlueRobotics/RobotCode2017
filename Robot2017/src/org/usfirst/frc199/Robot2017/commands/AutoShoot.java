package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Figures out speed at which to run motors (with static hood) for provided
 * distance to boiler. Uses ShooterPID to run motor at that speed for given
 * duration. Runs feeder motors while the shooter motor is with in 5% of the
 * target speed.
 * 
 * NOT TESTED
 */
public class AutoShoot extends Command {
	double target;
	double angle;
	Timer tim = new Timer();
	double duration;

	ShooterInterface shooter;

	/** 
	 * @param targetDistance - distance to the boiler
	 * @param runTime - duration to run the shooter motor
	 */

	public AutoShoot(double targetDistance, double runTime, ShooterInterface shooter) {
		requires(Robot.shooter);
		this.shooter = shooter;
		double[] targetandangle = this.shooter.convertDistanceToTargetVelocityAndAngle(targetDistance / 12 * 0.3048);
		target = targetandangle[0] * 12 / 0.3048; // inches per second
		angle = targetandangle[1];

		// converts in/s to rpm: in/s * s/min * circumference
		target = target * 60 * Robot.getPref("shooterWheelRadius", 4.5) * 2 * Math.PI;

		duration = runTime;
	}

	public void initialize() {
		tim.start();
		shooter.setHoodServo(angle);
	}

	public void execute() {
		if (duration != 6.9 || SmartDashboard.getBoolean("Vision/OH-YEAH", false)) {
			if (!shooter.shooterMotorStalled()) {
				shooter.runShootMotor(target);
			}
			if (Math.abs(shooter.getShooterSpeed() - target) <= Robot.getPref("speedErrorConstant", .05) * target) {
				shooter.runFeederMotor(1);
			}
		}

	}

	protected boolean isFinished() {
		return (tim.get() >= duration);
	}

	public void end() {
		shooter.stopShootMotor();
	}

	protected void interrupted() {
	}
}
