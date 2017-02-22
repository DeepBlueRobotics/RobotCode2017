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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements IntakeInterface {

	private final DoubleSolenoid pivotPiston = RobotMap.intakePivotPiston;
	private final DoubleSolenoid flipperFlapper = RobotMap.flipperFlapper;
	private final SpeedController intakeMotor = RobotMap.intakeIntakeMotor;
	
	private final AnalogInput AI = RobotMap.driverAI;

	private final PowerDistributionPanel pdp = RobotMap.pdp;
	private boolean AItriggered = false;
	private Timer tim = new Timer();
	private boolean intakeIsDown = true;
	private boolean flipperIsUp = false;

	public Intake(){
		super();
		putString("~TYPE~", "Intake");
	}
	
	public void initDefaultCommand() {
	}
	
	/**
	 * Returns the current value of the intakeMotor
	 */
	public double getIntake() {
		return intakeMotor.get();
	}

	/**
	 * Rruns the intake motor at a set speed
	 * 
	 * @param speed
	 *            - the speed you want the intake motor to run at -1 -> 1
	 */
	public void runIntake(double speed) {
		intakeMotor.set(speed);
	}
	
	/**
	 * Runs intake at a speed based on drive speed
	 * */
	public void controlledIntake(double speed) {
		intakeMotor.set(0.67 * (Robot.drivetrain.getRightSpeed() + Robot.drivetrain.getLeftSpeed())/2 + 0.33 * speed);
	}

	/**
	 * Returns whether or not the pdp detects the intake drawing more current than allowed
	 */
	public boolean intakeCurrentOverflow() {
		int channel = (int) (Robot.getPref("Intake PDP channel", 2));
		double current = pdp.getCurrent(channel);
		if (current >= Robot.getPref("maxIntakeCurrent", 40))
			return true;
		return false;
	}

	/**
	 * Stops the intake motor
	 */
	public void stopIntake() {
		intakeMotor.set(0);
	}

	/**
	 * Moves the intake up if it is down, and vice versa
	 */
	public void toggleIntake() {
		if (!intakeIsDown) {
			// shifts to intake up
			pivotPiston.set(DoubleSolenoid.Value.kReverse);
			intakeIsDown = true;
		} else {
			// shifts to intake down
			pivotPiston.set(DoubleSolenoid.Value.kForward);
			intakeIsDown = false;
		}
	}
	
	/**
	 * Sets the pivot piston to neutral
	 * */
	public void setIntakePistonNeutral(){
		pivotPiston.set(DoubleSolenoid.Value.kOff);
	}
	
	/**
	 * @return if the intake is up or not
	 * */
	public boolean intakeIsDown(){
		return intakeIsDown;
	}
	
	/**
	 * Sets flipperFlapper to forward unless it already is, then sets to backwards
	 */
	public void toggleFlipperFlapper() {
		if (!flipperIsUp) {
			// shifts flipper to down
			flipperFlapper.set(DoubleSolenoid.Value.kReverse);
			flipperIsUp = true;
		} else {
			// shifts flipper to up
			flipperFlapper.set(DoubleSolenoid.Value.kForward);
			flipperIsUp = false;
		}
	}
	
	/**
	 * Sets the flipperFlapper to neutral
	 * */
	public void setFlipperFlapperNeutral(){
		flipperFlapper.set(DoubleSolenoid.Value.kOff);
	}
	
	/**
	 * @return if the gear has been lifted or not
	 * */
	public boolean gearLifted(boolean isTriggered) {
		// return if gear lifted or not
		if(AI.getVoltage() > 0.15) {
			tim.reset();
			tim.start();
		}
		AItriggered = (tim.get() > 2);
		if(isTriggered) {
			return AItriggered;
		} else {
			return(AI.getVoltage() > 0.15);
		}
//		if (AI.getVoltage() > 0.15) {
//			AItriggered = true;
//			tim.reset();
//			tim.start();
//			return true;
//		} else {
//			if(tim.get() > 5) AItriggered = false;
//			return false;
//		}
	}
	
	/**
	 * Resets the light sensor trigger value
	 * */
	public void resetAITrigger() {
		AItriggered = false;
	}
	
	@Override
	/**
	 * Displays data to SmartDashboard
	 */
	public void displayData() {
		putString("Flap piston status", flipperFlapper.get().toString());
		putNumber("Intake current draw", pdp.getCurrent((int)Robot.getPref("Intake PDP channel", 2)));
		putNumber("Sending to intake", getIntake());
		putString("Intake piston status", pivotPiston.get().toString());
		putBoolean("Gear has been lifted", gearLifted(false));
		putNumber("Peg sensor reading", AI.getVoltage());
		putBoolean("Peg sensor has triggered", AItriggered);
	}
}
