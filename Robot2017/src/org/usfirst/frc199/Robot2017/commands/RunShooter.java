package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

/**
 *
 */
public class RunShooter extends Command {
	double speed = 0;
	ShooterInterface shooter;
	double duration;
	Timer tim = new Timer();

	public RunShooter(ShooterInterface shooter, double time) {
		this.shooter = shooter;
		duration = time;
		requires(Robot.shooter);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		tim.reset();
		tim.start();
//		requires(Robot.shooter);
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
//		if (!shooter.shooterMotorStalled()) {
		if (Robot.oi.manipulator.getRawButton(7)) {
			speed -= 0.02;
		}
		if (Robot.oi.manipulator.getRawButton(5)) {
			speed += 0.02;
		}
		shooter.runShootMotor(speed);
//		}
		if(tim.get() >= 1.5){
			shooter.runFeederMotor(.5);
			shooter.runFloorBeltMotor(.5);
		}
		
		shooter.runTurretMotor(Robot.oi.manipulator.getX());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (duration == 0) {
			return false;
		} else {
			return tim.get() >= duration;
		}
	}

	// Called once after isFinished returns true
	public void end() {
		shooter.runShootMotor(0);
		shooter.runTurretMotor(0);
		shooter.runFeederMotor(0);
		shooter.runFloorBeltMotor(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
