package org.usfirst.frc.team199.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	public boolean isInArcadeDrive = true;
    private double currentSpeed = 0; //only used and changed in Arcade Drive
    private double currentTurn = 0;
    
    public Joystick leftJoy;
    public Joystick rightJoy;
    public static RobotDrive robotDrive;
    
    public static SpeedController leftMotor;
    public static SpeedController rightMotor;
    
    public static AHRS gyro;
    

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
        leftJoy = new Joystick(0);
        rightJoy = new Joystick(1);
        
        robotDrive = new RobotDrive(leftMotor, rightMotor);
        
        gyro = new AHRS(SerialPort.Port.kMXP); // Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		gradualDrive();
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro Rate", gyro.getRate());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	
	public void gradualDrive() {
    	if (isInArcadeDrive) {
    		currentSpeed += Math.signum(rightJoy.getY() - currentSpeed) * 0.05;
    		currentTurn += Math.signum(leftJoy.getX() - currentTurn) * 0.05;
    		robotDrive.arcadeDrive(currentTurn, currentSpeed);
    	} else {
    		robotDrive.tankDrive(leftMotor.get() + Math.signum(leftJoy.getY() - leftMotor.get()) * 0.05, 
    				rightMotor.get() + Math.signum(rightJoy.getY() - rightMotor.get()) * 0.05);
    	}
    }
}

