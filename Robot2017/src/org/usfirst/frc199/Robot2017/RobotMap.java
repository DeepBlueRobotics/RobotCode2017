package org.usfirst.frc199.Robot2017;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
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
	private final static double shootFGain = 0.374;
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
	public static final boolean practice = Robot.getPref("Is practice robot?", false);
	
	public static void init() {
		
		if(practice)
		{
			drivetrainLeftMotor = new Talon(0);
			drivetrainRightMotor = new Talon(1);
			drivetrainLeftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
			drivetrainRightEncoder = new Encoder(3, 2, false, EncodingType.k4X);
			drivetrainGyro = new AnalogGyro(0);
			drivetrainCompressor = new Compressor(0);
			drivetrainShiftPiston = new DoubleSolenoid(0, 0, 1);
			intakePivotPiston = new DoubleSolenoid(0, 2, 3);
			intakeIntakeMotor = new Talon(2);
			shooterShootMotor = new Talon(3);
			shooterFeedMotor = new Talon(4);
			shooterShootEncoder = new Encoder(4, 5, false, EncodingType.k4X);
			hoodAngleEncoder = new Encoder(8, 9, false, EncodingType.k4X);
			turretTurnMotor = new Talon(6);
			turretTurretEncoder = new Encoder(6, 7, false, EncodingType.k4X);
			winchEncoder = new Encoder(11, 12, false, EncodingType.k4X);
			climberWinchMotor = new Talon(8);
			climberPlateIRSensor = new AnalogInput(1);
			driverAI = new AnalogInput(2);
			flipperFlapper = new DoubleSolenoid(3,4,5);
			drivetrainLeftUSsensor = new AnalogInput(3);
			drivetrainRightUSsensor = new AnalogInput(4);
		}
		else
		{
			drivetrainLeftMotor = new Talon(0);
			drivetrainRightMotor = new Talon(1);
			drivetrainLeftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
			drivetrainRightEncoder = new Encoder(3, 2, false, EncodingType.k4X);
			drivetrainGyro = new AnalogGyro(0);
			drivetrainCompressor = new Compressor(0);
			drivetrainShiftPiston = new DoubleSolenoid(0, 0, 1);
			intakePivotPiston = new DoubleSolenoid(0, 2, 3);
			intakeIntakeMotor = new Talon(2);
			shooterShootMotor = new Talon(3);
			shooterFeedMotor = new Talon(4);
			shooterShootEncoder = new Encoder(4, 5, false, EncodingType.k4X);
			hoodAngleEncoder = new Encoder(8, 9, false, EncodingType.k4X);
			turretTurnMotor = new Talon(6);
			turretTurretEncoder = new Encoder(6, 7, false, EncodingType.k4X);
			winchEncoder = new Encoder(11, 12, false, EncodingType.k4X);
			climberWinchMotor = new Talon(8);
			climberPlateIRSensor = new AnalogInput(1);
			driverAI = new AnalogInput(2);
			flipperFlapper = new DoubleSolenoid(3,4,5);
			drivetrainLeftUSsensor = new AnalogInput(3);
			drivetrainRightUSsensor = new AnalogInput(4);
		}
		
		LiveWindow.addActuator("Drivetrain", "LeftMotor", (Talon) drivetrainLeftMotor);
		drivetrainLeftMotor.setInverted(true);
		
		
		LiveWindow.addActuator("Drivetrain", "RightMotor", (Talon) drivetrainRightMotor);

		drivetrainRobotDrive = new RobotDrive(drivetrainLeftMotor, drivetrainRightMotor);

		drivetrainRobotDrive.setSafetyEnabled(true);
		drivetrainRobotDrive.setExpiration(0.1);
		drivetrainRobotDrive.setSensitivity(0.7);
		drivetrainRobotDrive.setMaxOutput(1.0);

		
		LiveWindow.addSensor("Drivetrain", "LeftEncoder", drivetrainLeftEncoder);
		drivetrainLeftEncoder.setDistancePerPulse(Robot.getPref("leftEncoderRatio", .0531));
		drivetrainLeftEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		LiveWindow.addSensor("Drivetrain", "RightEncoder", drivetrainRightEncoder);
		drivetrainRightEncoder.setDistancePerPulse(Robot.getPref("rightEncoderRatio", .0521));
		drivetrainRightEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		
		
		LiveWindow.addSensor("Drivetrain", "Gyro", drivetrainGyro);
		drivetrainGyro.setSensitivity(0.007);
		

		
		LiveWindow.addActuator("Drivetrain", "ShiftPiston", drivetrainShiftPiston);

		
		LiveWindow.addActuator("Intake", "PivotPiston", intakePivotPiston);
		
	
		LiveWindow.addActuator("Intake", "IntakeMotor", (Talon) intakeIntakeMotor);

		
		LiveWindow.addActuator("Shooter", "ShootMotor", (Talon) shooterShootMotor);

		
		LiveWindow.addActuator("Shooter", "FeedMotor", (Talon) shooterFeedMotor);

		
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
		
	
		LiveWindow.addSensor("Shooter", "HoodAngleEncoder", hoodAngleEncoder);
		hoodAngleEncoder.setDistancePerPulse(1.0);
		hoodAngleEncoder.setPIDSourceType(PIDSourceType.kRate);
		
	
		LiveWindow.addActuator("Shooter", "TurnMotor", (Talon) turretTurnMotor);
		
		LiveWindow.addSensor("Shooter", "TurretEncoder", turretTurretEncoder);
		turretTurretEncoder.setDistancePerPulse(1.0);
		turretTurretEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		
		LiveWindow.addSensor("Climber", "WinchEncoder", winchEncoder);
		winchEncoder.setDistancePerPulse(1.0);
		winchEncoder.setPIDSourceType(PIDSourceType.kRate);
	
		LiveWindow.addActuator("Climber", "WinchMotor", (Talon) climberWinchMotor);
		
		
		
		LiveWindow.addSensor("Climber", "ClimberInfrared", climberPlateIRSensor);
		
		
		LiveWindow.addSensor("Drivetrain", "ClimberInfrared", driverAI);

		
		LiveWindow.addActuator("Intake", "FlipperFlapper", flipperFlapper);
		
		
		LiveWindow.addSensor("Drivetrain", "LeftUltrasonic", drivetrainLeftUSsensor);
		
	
		LiveWindow.addSensor("Drivetrain", "RightUltrasonic", drivetrainRightUSsensor);
		
		// Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB
		ahrs = new AHRS(SerialPort.Port.kMXP); 
		pdp = new PowerDistributionPanel();
	}
}
