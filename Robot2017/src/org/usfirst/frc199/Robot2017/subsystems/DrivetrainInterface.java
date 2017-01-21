package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.commands.TeleopDrive;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public interface DrivetrainInterface {
	public void initDefaultCommand();
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
}
