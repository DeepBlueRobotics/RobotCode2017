package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements IntakeInterface {

	private final DoubleSolenoid pivotPiston = RobotMap.intakePivotPiston;
	private final SpeedController intakeMotor = RobotMap.intakeIntakeMotor;

	private boolean isPistonUp = false;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public void initDefaultCommand() {
	}

	/**
	 * This method runs the intake motor at a set speed
	 * 
	 * @param speed
	 *            - the speed you want the intake motor to run at -1 -> 1
	 */
	public void runIntake(double speed) {
		intakeMotor.set(speed);
	}

	/**
	 * This method stops the intake motor
	 */
	public void stopIntake() {
		intakeMotor.set(0);
	}

	/**
	 * This method moves the intake up if it is down, and vice versa
	 */
	public void toggleIntake() {
		isPistonUp = !isPistonUp;
		if (isPistonUp) {
			pivotPiston.set(DoubleSolenoid.Value.kForward);
		} else {
			pivotPiston.set(DoubleSolenoid.Value.kReverse);
		}
	}

	/**
	 * This method stops the DoubleSolenoid responsible for intaking
	 */
	public void stopIntakeDoubleSolenoid() {
		pivotPiston.set(DoubleSolenoid.Value.kOff);
	}

	@Override
	/**
	 * This method displays data to SmartDashboard
	 */
	public void displayData() {
		SmartDashboard.putBoolean("isPistonUp", isPistonUp);
	}
}
