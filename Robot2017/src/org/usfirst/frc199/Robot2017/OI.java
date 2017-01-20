package org.usfirst.frc199.Robot2017;

import org.usfirst.frc199.Robot2017.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import org.usfirst.frc199.Robot2017.subsystems.*;

public class OI {

	public JoystickButton switchDrive;
	public Joystick leftJoy;
	public JoystickButton shiftGears;
	public JoystickButton driveGradually;
	public JoystickButton autoShootRoutineButton;
	public Joystick rightJoy;
	public JoystickButton intakeButton;
	public JoystickButton outputButton;
	public JoystickButton shootOutButton;
	public JoystickButton winchInButton;
	public JoystickButton winchButton;
	public JoystickButton feedInButton;
	public JoystickButton feedOutButton;
	public JoystickButton toggleIntakeButton;
	public Joystick manipulator;

	public OI() {

		manipulator = new Joystick(2);

		feedOutButton = new JoystickButton(manipulator, 8);
		feedOutButton.whileHeld(new RunFeeder(Robot.getPref("feederDirection", 1)));
		feedInButton = new JoystickButton(manipulator, 6);
		feedInButton.whileHeld(new RunFeeder(-Robot.getPref("feederDirection", 1)));
		winchButton = new JoystickButton(manipulator, 2);
		winchButton.whileHeld(new Climb());
		shootOutButton = new JoystickButton(manipulator, 4);
		shootOutButton.whileHeld(new RunShooter(Robot.getPref("shooterDirection", 1)));
		outputButton = new JoystickButton(manipulator, 5);
		outputButton.whileHeld(new RunIntake(Robot.getPref("intakeDirection", 1)));
		intakeButton = new JoystickButton(manipulator, 7);
		intakeButton.whileHeld(new RunIntake(-Robot.getPref("intakeDirection", 1)));
		toggleIntakeButton = new JoystickButton(manipulator, 3);
		toggleIntakeButton.whenPressed(new ToggleIntake());
		rightJoy = new Joystick(1);

		autoShootRoutineButton = new JoystickButton(rightJoy, 4);
		// TODO: (Ana T.) Insert accurate AutoShoot arguments to be executed
		// when autoShootRoutineButton is held
		autoShootRoutineButton.whileHeld(new AutoShoot(0, 0));
		driveGradually = new JoystickButton(rightJoy, 1);
		driveGradually.whileHeld(new GradualDrive());
		shiftGears = new JoystickButton(rightJoy, 2);
		shiftGears.whenPressed(new ToggleDrivetrainShift());
		leftJoy = new Joystick(0);

		switchDrive = new JoystickButton(leftJoy, 2);
		switchDrive.whenPressed(new ToggleDriveType());

		// SmartDashboard Buttons
		SmartDashboard.putData("MainAutoMode", new MainAutoMode());
		SmartDashboard.putData("AutoDrive", new AutoDrive(0, 0));
		SmartDashboard.putData("AutoAdjustHood", new AutoAdjustHood(0));
		SmartDashboard.putData("AutoAdjustTurret", new AutoAdjustTurret(0));
		SmartDashboard.putData("AutoShoot", new AutoShoot(0, 0));
		SmartDashboard.putData("TeleopDrive", new TeleopDrive());
		SmartDashboard.putData("GradualDrive", new GradualDrive());
		SmartDashboard.putData("ToggleDriveType", new ToggleDriveType());
		SmartDashboard.putData("ToggleDrivetrainShift", new ToggleDrivetrainShift());
		SmartDashboard.putData("TestPID", new TestPID());
		SmartDashboard.putData("IntakeIn", new RunIntake(0));
		SmartDashboard.putData("FeederIn", new RunFeeder(0));
		SmartDashboard.putData("TurnTurret", new TurnTurret());
		SmartDashboard.putData("ToggleIntake", new ToggleIntake());
		SmartDashboard.putData("Climb", new Climb());
		
		//For use by Manual Control Widget
		SmartDashboard.putData("ManualControl/Command", new ManualControlMechs());

	}

	public Joystick getLeftJoy() {
		return leftJoy;
	}

	public Joystick getRightJoy() {
		return rightJoy;
	}

	public Joystick getManipulator() {
		return manipulator;
	}

}
