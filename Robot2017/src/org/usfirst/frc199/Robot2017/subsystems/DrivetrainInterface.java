package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.commands.TeleopDrive;
import org.usfirst.frc199.Robot2017.motion.PID;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public interface DrivetrainInterface extends DashboardInterface {
	public void initDefaultCommand();
	
	public double convertVoltsToInches(double volts);
	
	public double getUSVoltage(boolean side);
	
	public double getUSDistToDrive();
	
	public double calcUSTargetAngle();

	public PID getDrivePID();
	
	public PID getTurnPID();
	
	public void resetEncoder();

	public void resetGyro();

	public double getAngle();

	public double getDistance();

	public double getSpeed();

	public boolean distanceReachedTarget();

	public boolean angleReachedTarget();

	public void updateAngle();

	public void setDistanceTarget(double targetDistance);

	public void setAngleTarget(double targetAngle);

	public void gradualDrive();

	public void drive();

	public void autoDrive();

	public void stopDrive();

	public void shiftGears();

	public boolean currentControl();
	
	public void setVelocityTarget(double linVelTarget, double angVelTarget);
	
	public void updateVelocity();
	
	public DriveTypes getDriveType();
	
	public void toggleDriveType();
	
	public enum DriveTypes {
		ARCADE, TANK, DRIFT_TANK
	}
}
