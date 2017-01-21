package org.usfirst.frc199.Robot2017.subsystems;

public interface ClimberInterface {
	public void initDefaultCommand();

	public void runClimber(double speed);

	public boolean returnPlate();

	public void stopWinch();
	
	public void encoderReset();
	
	public double getEncoder();

}
