package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem implements ClimberInterface {

	private final SpeedController winchMotor = RobotMap.climberWinchMotor;
//	private final AnalogInput plateLimit = RobotMap.climberPlateIRSensor;
//	private final Encoder winchEncoder = RobotMap.winchEncoder;
	private final PowerDistributionPanel pdp = RobotMap.pdp;
	public boolean AIEnabled = false;

	public Climber(){
		super();
		putString("~TYPE~", "Climber");
	}
	
	public void initDefaultCommand() {
		
	}
//	/**
//	 * 
//	 * @return - returns the value of AIEnabled
//	 */
//	public boolean getAIEnabled() {
//		return AIEnabled;
//	}
//	/**
//	 * this method sets the value of AIEnabled
//	 */
//	public void setAIEnabled(boolean value) {
//		AIEnabled = value;
//	}
	/**
	 * This method uses the winch to let the robot climb
	 * 
	 * @param speed
	 *            - the speed that you want the winch to run on -1 -> 1
	 */
	public void runClimber(double speed) {
		winchMotor.set(speed);
	}
	public double getClimber() {
		return winchMotor.get();
	}

//	/**
//	 * This method returns whether the plate is sensing being touched
//	 * 
//	 * @return
//	 */
//	public boolean returnPlate() {
//		if (plateLimit.getVoltage() < 0.102 && AIEnabled) {
//			return true;
//		}
//		return false;
//	}

	public boolean checkMotorDraw() {
		int channel = (int) (Robot.getPref("climber channel", 1));
		double current = pdp.getCurrent(channel);
		if (current >= (int) Robot.getPref("maxClimberCurrent", 50)) {
			return true;
		}
		return false;
	}
	/**
	 * This method stops the winch
	 */
	public void stopWinch() {
		winchMotor.set(0);
	}
//	public void encoderReset() {
//		winchEncoder.reset();
//	}
//	public double getEncoder() {
//		//TODO: anyone, set distance per pulse to inches per pulse
//		return winchEncoder.getDistance();
//	}

	@Override
	/**
	 * This method displays data to SmartDashboard
	 */
	public void displayData() {
//		SmartDashboard.putBoolean("plateLimit", plateLimit.get());
//		putNumber("Encoder", getEncoder());
//		putBoolean("TouchingPlate", returnPlate());
		putNumber("Winch set speed", getClimber());
		putNumber("Winch current draw", pdp.getCurrent((int)Robot.getPref("climber channel", 1)));
	}
}
