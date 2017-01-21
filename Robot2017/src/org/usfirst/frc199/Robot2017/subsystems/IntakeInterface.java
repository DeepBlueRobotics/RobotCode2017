package org.usfirst.frc199.Robot2017.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public interface IntakeInterface {
	public void initDefaultCommand();
	public void runIntake(double speed);
	public void stopIntake();
	public void toggleIntake();
	public void stopIntakeDoubleSolenoid();
}
