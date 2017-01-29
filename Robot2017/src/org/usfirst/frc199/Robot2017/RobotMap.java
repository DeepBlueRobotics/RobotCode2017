package org.usfirst.frc199.Robot2017;

import com.kauailabs.navx.frc.AHRS;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;

public class RobotMap {
	public static SpeedController drivetrainLeftMotor;
	public static SpeedController drivetrainRightMotor;
	public static RobotDrive drivetrainRobotDrive;
	public static Encoder drivetrainLeftEncoder;
	public static Encoder drivetrainRightEncoder;
	public static AnalogGyro drivetrainGyro;
	public static Compressor drivetrainCompressor;
	public static DoubleSolenoid drivetrainShiftPiston;
	public static DigitalInput gearLiftedLimitSwitch;
	public static DoubleSolenoid intakePivotPiston;
	public static SpeedController intakeIntakeMotor;
	public static SpeedController shooterShootMotor;
	public static SpeedController shooterFeedMotor;
	public static Encoder shooterShootEncoder;
	public static SpeedController turretTurnMotor;
	public static Encoder turretTurretEncoder;
	public static SpeedController hoodAngleMotor;
	public static Encoder hoodAngleEncoder;
	public static SpeedController climberWinchMotor;
	public static AnalogInput climberPlateIRSensor;
	public static Encoder winchEncoder;
	public static AHRS ahrs;
	public static PowerDistributionPanel pdp;
	public static AnalogInput driverAI;

	public static void init() {
		drivetrainLeftMotor = new Talon(0);
		LiveWindow.addActuator("Drivetrain", "LeftMotor", (Talon) drivetrainLeftMotor);
		drivetrainLeftMotor.setInverted(true);
		
		drivetrainRightMotor = new Talon(1);
		LiveWindow.addActuator("Drivetrain", "RightMotor", (Talon) drivetrainRightMotor);

		drivetrainRobotDrive = new RobotDrive(drivetrainLeftMotor, drivetrainRightMotor);

		drivetrainRobotDrive.setSafetyEnabled(true);
		drivetrainRobotDrive.setExpiration(0.1);
		drivetrainRobotDrive.setSensitivity(0.7);
		drivetrainRobotDrive.setMaxOutput(1.0);

		drivetrainLeftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
		LiveWindow.addSensor("Drivetrain", "LeftEncoder", drivetrainLeftEncoder);
		drivetrainLeftEncoder.setDistancePerPulse(Robot.getPref("leftEncoderRatio", 1));
		drivetrainLeftEncoder.setPIDSourceType(PIDSourceType.kRate);
		drivetrainRightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		LiveWindow.addSensor("Drivetrain", "RightEncoder", drivetrainRightEncoder);
		drivetrainRightEncoder.setDistancePerPulse(Robot.getPref("rightEncoderRatio", 1));
		drivetrainRightEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		
		drivetrainGyro = new AnalogGyro(0);
		LiveWindow.addSensor("Drivetrain", "Gyro", drivetrainGyro);
		drivetrainGyro.setSensitivity(0.007);
		drivetrainCompressor = new Compressor(0);

		drivetrainShiftPiston = new DoubleSolenoid(0, 0, 1);
		LiveWindow.addActuator("Drivetrain", "ShiftPiston", drivetrainShiftPiston);

		intakePivotPiston = new DoubleSolenoid(0, 4, 5);
		LiveWindow.addActuator("Intake", "PivotPiston", intakePivotPiston);

		intakeIntakeMotor = new Talon(2);
		LiveWindow.addActuator("Intake", "IntakeMotor", (Talon) intakeIntakeMotor);

		shooterShootMotor = new Talon(3);
		LiveWindow.addActuator("Shooter", "ShootMotor", (Talon) shooterShootMotor);

		shooterFeedMotor = new Talon(4);
		LiveWindow.addActuator("Shooter", "FeedMotor", (Talon) shooterFeedMotor);

		shooterShootEncoder = new Encoder(4, 5, false, EncodingType.k4X);
		LiveWindow.addSensor("Shooter", "ShootEncoder", shooterShootEncoder);
		shooterShootEncoder.setDistancePerPulse(1.0);
		shooterShootEncoder.setPIDSourceType(PIDSourceType.kRate);
		turretTurnMotor = new Talon(6);
		LiveWindow.addActuator("Turret", "TurnMotor", (Talon) turretTurnMotor);

		turretTurretEncoder = new Encoder(6, 7, false, EncodingType.k4X);
		LiveWindow.addSensor("Turret", "TurretEncoder", turretTurretEncoder);
		turretTurretEncoder.setDistancePerPulse(1.0);
		turretTurretEncoder.setPIDSourceType(PIDSourceType.kRate);
		climberWinchMotor = new Talon(8);
		LiveWindow.addActuator("Climber", "WinchMotor", (Talon) climberWinchMotor);

		gearLiftedLimitSwitch = new DigitalInput(10);
		LiveWindow.addSensor("Drivetrain", "GearLimit", gearLiftedLimitSwitch);
		
		climberPlateIRSensor = new AnalogInput(0);
		LiveWindow.addSensor("Climber", "ClimberInfrared", climberPlateIRSensor);
		
		driverAI = new AnalogInput(1);
		LiveWindow.addSensor("Drivetrain", "ClimberInfrared", driverAI);

		// Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB
		ahrs = new AHRS(SerialPort.Port.kMXP); 
		pdp = new PowerDistributionPanel();
	}
}
