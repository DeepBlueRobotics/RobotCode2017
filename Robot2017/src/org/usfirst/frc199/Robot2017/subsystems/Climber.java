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
	private final PowerDistributionPanel pdp = RobotMap.pdp;

	public Climber() {
		super();
		putString("~TYPE~", "Climber");
	}

	public void initDefaultCommand() {

	}

	/**
	 * Uses the winch to let the robot climb.
	 * @param speed the speed that you want the winch to run on between -1 and 1
	 */
	public void runClimber(double speed) {
		winchMotor.set(speed);
	}

	/**
	 * @return the speed of the climber motor between -1 and 1
	 */
	public double getClimber() {
		return winchMotor.get();
	}

	/**
	 * @return if the climber motor current is above the max current
	 */
	public boolean checkMotorDraw() {
		int channel = (int) (Robot.getPref("climber channel", 1));
		double current = pdp.getCurrent(channel);
		if (current >= (int) Robot.getPref("maxClimberCurrent", 50)) {
			return true;
		}
		return false;
	}

	/**
	 * Stops the winch
	 */
	public void stopWinch() {
		winchMotor.set(0);
	}

	@Override
	/**
	 * Displays climber motor data to SmartDashboard
	 */
	public void displayData() {
		putNumber("Winch set speed", getClimber());
		putNumber("Winch current draw", pdp.getCurrent((int) Robot.getPref("climber channel", 1)));
	}
}
