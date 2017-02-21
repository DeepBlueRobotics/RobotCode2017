package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements IntakeInterface {

	private final DoubleSolenoid pivotPiston = RobotMap.intakePivotPiston;
	private final DoubleSolenoid flipperFlapper = RobotMap.flipperFlapper;
	private final SpeedController intakeMotor = RobotMap.intakeIntakeMotor;
	
	private final AnalogInput AI = RobotMap.driverAI;

	private final PowerDistributionPanel pdp = RobotMap.pdp;

	public Intake(){
		super();
		putString("~TYPE~", "Intake");
	}
	
	public void controlledIntake(double speed) {
		intakeMotor.set(0.67 * (Robot.drivetrain.getRightSpeed() + Robot.drivetrain.getLeftSpeed())/2 + 0.33 * speed);
	}
	
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
		if (!pivotPiston.get().toString().equals("kForward")) {
			pivotPiston.set(DoubleSolenoid.Value.kForward);
		} else {
			pivotPiston.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	/**
	 * @return if the intake is up or not
	 * */
	public boolean intakeIsUp(){
		return pivotPiston.get().toString().equals("kReverse");
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
	
	public boolean gearLifted() {
		// return if gear lifted or not
		if (AI.getVoltage() > 0.19) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method sets flipperFlapper to forward unless it already is, then sets to backwards
	 */
	public void toggleFlipperFlapper() {
		if (!flipperFlapper.get().toString().equals("kForward")) {
			flipperFlapper.set(DoubleSolenoid.Value.kForward);
		} else {
			flipperFlapper.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	/**
	 * This method stops the flipperFlapper
	 */
	public void stopFlipperFlapper() {
		flipperFlapper.set(DoubleSolenoid.Value.kOff);
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
		putString("Flap piston status", flipperFlapper.get().toString());
		putNumber("Intake current draw", pdp.getCurrent((int)Robot.getPref("Intake PDP channel", 2)));
		putNumber("Sending to intake", getIntake());
		putString("Intake piston status", pivotPiston.get().toString());
		putBoolean("Gear has been lifted", gearLifted());
		putNumber("Peg sensor reading", AI.getVoltage());
	}
}
