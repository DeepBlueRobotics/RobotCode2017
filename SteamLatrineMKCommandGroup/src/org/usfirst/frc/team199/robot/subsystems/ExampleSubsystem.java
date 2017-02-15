package org.usfirst.frc.team199.robot.subsystems;

import org.usfirst.frc.team199.robot.RobotMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ExampleSubsystem extends Subsystem {
	private SpeedController theMotor = RobotMap.theMotor;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void runMotor(double speed) {
		theMotor.set(speed);
	}
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
