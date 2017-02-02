package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;

public interface ClimberInterface extends DashboardInterface {
	public void initDefaultCommand();

	public void runClimber(double speed);

	public boolean returnPlate();

	public void stopWinch();

	public void encoderReset();

	public double getEncoder();
	
	public boolean checkMotorDraw();
	
	public double getClimber();
	
	public boolean getAIEnabled();
	
	public void setAIEnabled(boolean value);
//	boolean AIEnabled = false;


}
