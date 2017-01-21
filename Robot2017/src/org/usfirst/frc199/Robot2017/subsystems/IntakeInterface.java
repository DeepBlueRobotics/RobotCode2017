package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public interface IntakeInterface extends DashboardInterface {
	public void initDefaultCommand();

	public void runIntake(double speed);

	public void stopIntake();

	public void toggleIntake();

	public void stopIntakeDoubleSolenoid();
}
