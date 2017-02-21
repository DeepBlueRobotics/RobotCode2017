package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

/**
 * Causes the PID code for a specific subsystem to attempt to reach a value
 * specified on the SmartDashboard.
 */
public class TestPID extends Command {

	private final System system;
	private double target = 0;
	
	DrivetrainInterface drivetrain;
	ShooterInterface shooter;

	// The various PID loops of the robot
	public enum System {
		DRIVEDISTANCE, DRIVEANGLE, SHOOTER, DRIVEVELOCITY, DRIVEANGULARVELOCITY, LEFTDRIVEVELOCITY, RIGHTDRIVEVELOCITY;
	}

	/**
	 * @param system - The PID system to be tested
	 */
	public TestPID(System system, ShooterInterface shooter, DrivetrainInterface driver) {
		this.system = system;
		this.drivetrain = driver;
		this.shooter = shooter;
		switch(system) {
			case SHOOTER: requires(Robot.shooter); break;
			default: requires(Robot.drivetrain);
		}
	}

	// Called just before this Command runs the first time
	// Uncomment the SmartDashboard before use, commented out b/c of test issues.
	public void initialize() {
		switch(system) {
			case DRIVEDISTANCE:
				target = SmartDashboard.getNumber("PID/DriveDistance/TestTarget" , 0);
				drivetrain.setDistanceTarget(target);
				drivetrain.setAngleTarget(0);
				break;
			case DRIVEANGLE:
				target = SmartDashboard.getNumber("PID/DriveAngle/TestTarget" , 0);
				drivetrain.setAngleTarget(target);
				break;
			case SHOOTER:
				target = SmartDashboard.getNumber("PID/Shooter/TestTarget" , 0);
				shooter.setShooterPIDTarget(target);
				shooter.updateShooterPID(shooter.getShooterSpeed());
				break;
			case DRIVEVELOCITY:
				target = SmartDashboard.getNumber("PID/DriveVelocity/TestTarget", 0);
				drivetrain.setVelocityTarget(target, 0);
				break;
			case DRIVEANGULARVELOCITY:
				target = SmartDashboard.getNumber("PID/DriveAngularVelocity/TestTarget", 0);
				drivetrain.setVelocityTarget(0, target);
				break;
			case LEFTDRIVEVELOCITY:
				target = SmartDashboard.getNumber("PID/LeftDriveVelocity/TestTarget", 0);
				drivetrain.setLeftSpeedTarget(target);
				break;
			case RIGHTDRIVEVELOCITY:
				target = SmartDashboard.getNumber("PID/RightDriveVelocity/TestTarget", 0);
				drivetrain.setRightSpeedTarget(target);
				break;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		switch(system) {
			case SHOOTER: shooter.runShootMotor(shooter.updateSpeed(target)); break;
			case DRIVEDISTANCE: drivetrain.autoDrive(); break;
			case DRIVEANGLE: drivetrain.updateAnglePID(); break;
			case DRIVEVELOCITY: drivetrain.updateVelocityPIDs(); break;
			case DRIVEANGULARVELOCITY: drivetrain.updateVelocityPIDs(); break;
			case LEFTDRIVEVELOCITY: drivetrain.updateLeftSpeedPID(); break;
			case RIGHTDRIVEVELOCITY: drivetrain.updateRightSpeedPID(); break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		switch(system) {
			case SHOOTER: return false;
			case DRIVEDISTANCE: return drivetrain.distanceReachedTarget() && 
					drivetrain.angleReachedTarget();
			case DRIVEANGLE: return drivetrain.angleReachedTarget();
			case DRIVEVELOCITY: return false;
			case DRIVEANGULARVELOCITY: return false;
			case LEFTDRIVEVELOCITY: return false;
			case RIGHTDRIVEVELOCITY: return false;
			default: return false;
		}
	}

	// Called once after isFinished returns true
	public void end() {
		switch(system) {
			case SHOOTER: shooter.runShootMotor(0); break;
			default: drivetrain.stopDrive();
		}
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
