package org.usfirst.frc199.Robot2017;

import org.usfirst.frc199.Robot2017.commands.TestPID;
import org.usfirst.frc199.Robot2017.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import org.usfirst.frc199.Robot2017.subsystems.*;

public class OI {
	public JoystickButton switchDriveButton;
	public JoystickButton autoAlignGearRoutineButton;
	public Joystick leftJoy;
	public JoystickButton shiftGearsButton;
	public JoystickButton gradualDriveButton;
	public JoystickButton autoShootRoutineButton;
	public JoystickButton takePicButton;
	public JoystickButton intakeUpButton;
	public Joystick rightJoy;
	public JoystickButton intakeButton;
	public JoystickButton turnTurretButton;
	public JoystickButton shootOutButton;
	public JoystickButton winchInButton;
	public JoystickButton winchButton;
	public JoystickButton feedInButton;
	public JoystickButton feedOutButton;
	public JoystickButton deployGearButton;
	public JoystickButton toggleAndRunIntakeButton;
	public JoystickButton autoUSAdjustButton;
	public Joystick manipulator;
	public JoystickButton toggleFlipper;
	public JoystickButton intakeGearButton;
	public JoystickButton shootFromHopperButton;
	public JoystickButton shootFromAirshipButton;

	public OI() {
		manipulator = new Joystick(2);

//		feedOutButton = new JoystickButton(manipulator, 8);
//		feedOutButton.whileHeld(new RunFeeder(Robot.getPref("feederDirection", 1), Robot.shooter));
//		feedInButton = new JoystickButton(manipulator, 6);
//		feedInButton.whileHeld(new RunFeeder(-Robot.getPref("feederDirection", 1), Robot.shooter));
		winchButton = new JoystickButton(manipulator, 1);
		winchButton.whileHeld(new Climb(Robot.climber));
		shootOutButton = new JoystickButton(manipulator, 6);
		shootOutButton.whileHeld(new RunShooter(Robot.shooter, 0));
		turnTurretButton = new JoystickButton(manipulator, 8);
		turnTurretButton.whileHeld(new TurnTurret(manipulator.getX(), Robot.shooter));
//		outputButton = new JoystickButton(manipulator, 7);
//		outputButton.whileHeld(new RunIntake(Robot.getPref("intakeDirection", 1), true, Robot.intake));
//		intakeButton = new JoystickButton(manipulator, 6);
		
		//uncomment this line below when intake put back on
//		intakeButton.whileHeld(new RunIntake(-Robot.getPref("intakeDirection", 1), false, Robot.intake));

		intakeGearButton = new JoystickButton(manipulator, 2);
		intakeGearButton.whenPressed(new PickupGear());
		
		toggleAndRunIntakeButton = new JoystickButton(manipulator, 3);
		toggleAndRunIntakeButton.whenPressed(new ToggleIntake(false, true, Robot.intake));
		
		deployGearButton = new JoystickButton(manipulator, 4);
		deployGearButton.whileHeld(new DeployGear());
		
		//uncomment this line below when static gear intake put back on
		toggleFlipper = new JoystickButton(manipulator, 5);
		toggleFlipper.whenPressed(new ToggleIntakeRamp(false, false, Robot.intake));
		
//		autoUSAdjustButton = new JoystickButton(manipulator, 8);
//		autoUSAdjustButton.whileHeld(new AutoDrive(Robot.drivetrain.getUSDistToDrive(),
//				Robot.drivetrain.getUSTargetAngle(), Robot.drivetrain));

		rightJoy = new Joystick(1);

		autoShootRoutineButton = new JoystickButton(rightJoy, 4);
		autoShootRoutineButton.whileHeld(new AutoShootRoutine());
		gradualDriveButton = new JoystickButton(rightJoy, 1);
		gradualDriveButton.whileHeld(new GradualDrive(Robot.drivetrain));
		shiftGearsButton = new JoystickButton(rightJoy, 2);
		shiftGearsButton.whenPressed(new ToggleDrivetrainShift(Robot.drivetrain));
		takePicButton = new JoystickButton(rightJoy, 10);
		takePicButton.whileHeld(new WriteToNT("SmartDashboard/Vision/takePicture", true));
		takePicButton.whenReleased(new WriteToNT("SmartDashboard/Vision/takePicture", false));
		intakeUpButton = new JoystickButton(rightJoy, 1);
		toggleAndRunIntakeButton.whenPressed(new ToggleIntake(true, false, Robot.intake));

		leftJoy = new Joystick(0);

		shootFromHopperButton = new JoystickButton(leftJoy, 3);
		shootFromHopperButton.whenPressed(new SetTurretToShootingFromHopper(Robot.shooter));
		shootFromAirshipButton = new JoystickButton(leftJoy, 5);
		shootFromAirshipButton.whenPressed(new SetTurretToShootingFromLift(Robot.shooter));
		switchDriveButton = new JoystickButton(leftJoy, 4);
		switchDriveButton.whenPressed(new ToggleDriveType(Robot.drivetrain));
		autoAlignGearRoutineButton = new JoystickButton(leftJoy, 2);
//		autoAlignGearRoutineButton.whileHeld(new AutoAlignGear(false));
		
		//uncomment this line below when intake put back on
		autoAlignGearRoutineButton.whileHeld(new AutoDeliverGear(Robot.drivetrain, Robot.intake));

		
//		when the below gets reincorporated, change button cause already used >:)
//		autoAlignGearRoutineButton = new JoystickButton(leftJoy, 2);
//		autoAlignGearRoutineButton.whileHeld(new AutoDeliverGear(Robot.drivetrain, Robot.intake));
		
		// For use by Manual Control Widget
		SmartDashboard.putData("ManualControl/Command",
				new ManualControlMechs(Robot.intake, Robot.shooter, Robot.climber));

		// For use by PID Widget
		SmartDashboard.putData("PID/DriveDistance/TestDriveDistancePID",
				new TestPID(TestPID.System.DRIVEDISTANCE, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/DriveAngle/TestDriveAnglePID",
				new TestPID(TestPID.System.DRIVEANGLE, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/Shooter/TestShooterPID",
				new TestPID(TestPID.System.SHOOTER, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/DriveVelocity/TestDriveVelocityPID",
				new TestPID(TestPID.System.DRIVEVELOCITY, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/DriveAngularVelocity/TestDriveAngularVelocityPID",
				new TestPID(TestPID.System.DRIVEANGULARVELOCITY, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/LeftDriveVelocity/TestLeftDriveVelocityPID",
				new TestPID(TestPID.System.LEFTDRIVEVELOCITY, Robot.shooter, Robot.drivetrain));
		SmartDashboard.putData("PID/RightDriveVelocity/TestRightDriveVelocityPID",
				new TestPID(TestPID.System.RIGHTDRIVEVELOCITY, Robot.shooter, Robot.drivetrain));

		// For Trajectory Following Testing
		SmartDashboard.putData("FollowTrajectory", new FollowTrajectory());
		SmartDashboard.putData("setTurretToZero", new SetTurretToZero(Robot.shooter));

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
