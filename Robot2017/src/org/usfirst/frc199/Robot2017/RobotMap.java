package org.usfirst.frc199.Robot2017;

import com.kauailabs.navx.frc.AHRS;

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
	public static DoubleSolenoid intakePivotPiston;
	public static SpeedController intakeIntakeMotor;
	public static SpeedController shooterShootMotor;
	public static SpeedController shooterFeedMotor;
	public static Encoder shooterShootEncoder;
	public static CANTalon shooterShootMotorAndEncoder;
	private static final int CANTalonIDNum = 0;
	private final double shootFGain = 0.374;
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
	public static DoubleSolenoid flipperFlapper;
	public static AnalogInput drivetrainLeftUSsensor;
	public static AnalogInput drivetrainRightUSsensor;

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
		drivetrainLeftEncoder.setDistancePerPulse(Robot.getPref("leftEncoderRatio", .0531));
		drivetrainLeftEncoder.setPIDSourceType(PIDSourceType.kRate);
		drivetrainRightEncoder = new Encoder(3, 2, false, EncodingType.k4X);
		LiveWindow.addSensor("Drivetrain", "RightEncoder", drivetrainRightEncoder);
		drivetrainRightEncoder.setDistancePerPulse(Robot.getPref("rightEncoderRatio", .0521));
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
		
		shooterShootMotorAndEncoder = new CANTalon(CANTalonIDNum);
		LiveWindow.addSensor("Shooter", "ShootMotorAndEncoder", shooterShootMotorAndEncoder);
		shooterShootMotorAndEncoder.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooterShootMotorAndEncoder.reverseSensor(false);
		shooterShootMotorAndEncoder.setF(shootFGain);
		shooterShootMotorAndEncoder.configNominalOutputVoltage(+0.0f, -0.0f);
		shooterShootMotorAndEncoder.configPeakOutputVoltage(+12.0f, -12.0f);
		shooterShootMotorAndEncoder.changeControlMode(TalonControlMode.Speed);
		
		hoodAngleEncoder = new Encoder(8, 9, false, EncodingType.k4X);
		LiveWindow.addSensor("Shooter", "HoodAngleEncoder", hoodAngleEncoder);
		hoodAngleEncoder.setDistancePerPulse(1.0);
		hoodAngleEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		turretTurnMotor = new Talon(6);
		LiveWindow.addActuator("Shooter", "TurnMotor", (Talon) turretTurnMotor);
		turretTurretEncoder = new Encoder(6, 7, false, EncodingType.k4X);
		LiveWindow.addSensor("Shooter", "TurretEncoder", turretTurretEncoder);
		turretTurretEncoder.setDistancePerPulse(1.0);
		turretTurretEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		winchEncoder = new Encoder(11, 12, false, EncodingType.k4X);
		LiveWindow.addSensor("Climber", "WinchEncoder", winchEncoder);
		winchEncoder.setDistancePerPulse(1.0);
		winchEncoder.setPIDSourceType(PIDSourceType.kRate);
		climberWinchMotor = new Talon(8);
		LiveWindow.addActuator("Climber", "WinchMotor", (Talon) climberWinchMotor);
		
		
		climberPlateIRSensor = new AnalogInput(1);
		LiveWindow.addSensor("Climber", "ClimberInfrared", climberPlateIRSensor);
		
		driverAI = new AnalogInput(2);
		LiveWindow.addSensor("Drivetrain", "ClimberInfrared", driverAI);

		flipperFlapper = new DoubleSolenoid(3,4,5);
		LiveWindow.addActuator("Intake", "FlipperFlapper", flipperFlapper);
		
		drivetrainLeftUSsensor = new AnalogInput(3);
		LiveWindow.addSensor("Drivetrain", "LeftUltrasonic", drivetrainLeftUSsensor);
		
		drivetrainRightUSsensor = new AnalogInput(4);
		LiveWindow.addSensor("Drivetrain", "RightUltrasonic", drivetrainRightUSsensor);
		
		// Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB
		ahrs = new AHRS(SerialPort.Port.kMXP); 
		pdp = new PowerDistributionPanel();
	}
}
