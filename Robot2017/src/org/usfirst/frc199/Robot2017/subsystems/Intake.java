package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements IntakeInterface {

	private final DoubleSolenoid pivotPiston = RobotMap.intakePivotPiston;
	private final SpeedController intakeMotor = RobotMap.intakeIntakeMotor;

	private final PowerDistributionPanel pdp = RobotMap.pdp;
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

	/**
	 * This method returns whether or not the pdp detects the intake drawing more current than allowed
	 */
	public boolean intakeCurrentOverflow() {
		int channel = (int) (Robot.getPref("Intake PDP channel", 2));
		double current = pdp.getCurrent(channel);
		if (current >= Robot.getPref("maxIntakeCurrent", 40))
			return true;
		return false;
	}
	/**
	 * This method returns the current value of the intakeMotor
	 */
	public double getIntake() {
		return intakeMotor.get();
	}
	@Override
	/**
	 * This method displays data to SmartDashboard
	 */
	public void displayData() {
		SmartDashboard.putBoolean("isPistonUp", isPistonUp);
		SmartDashboard.putNumber("intakeCurrent", pdp.getCurrent((int)Robot.getPref("Intake PDP channel", 2)));
	}
}
