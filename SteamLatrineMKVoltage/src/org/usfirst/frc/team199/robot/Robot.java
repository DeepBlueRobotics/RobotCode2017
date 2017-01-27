package org.usfirst.frc.team199.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	VictorSP testMotor1 = new VictorSP(1);
	VictorSP testMotor2 = new VictorSP(2);
	VictorSP testMotor3 = new VictorSP(3);
	VictorSP testMotor4 = new VictorSP(4);
	VictorSP testMotor5 = new VictorSP(5);
	VictorSP testMotor6 = new VictorSP(6);
	VictorSP testMotor7 = new VictorSP(7);
	VictorSP testMotor8 = new VictorSP(8);
	VictorSP testMotor9 = new VictorSP(9);
	VictorSP testMotor0 = new VictorSP(0);
	PowerDistributionPanel pdp = new PowerDistributionPanel();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
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
		switch((int)SmartDashboard.getNumber("motor", 0)) {
			case 0: testMotor0.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 1: testMotor1.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 2: testMotor2.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 3: testMotor3.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 4: testMotor4.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 5: testMotor5.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 6: testMotor6.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 7: testMotor7.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 8: testMotor8.set(SmartDashboard.getNumber("motorValue", 0));
			break;
			case 9: testMotor9.set(SmartDashboard.getNumber("motorValue", 0));
			break;
		
		}
		SmartDashboard.putNumber("port0", pdp.getCurrent(0));
		SmartDashboard.putNumber("port1", pdp.getCurrent(1));
		SmartDashboard.putNumber("port2", pdp.getCurrent(2));
		SmartDashboard.putNumber("port3", pdp.getCurrent(3));
		SmartDashboard.putNumber("port4", pdp.getCurrent(4));
		SmartDashboard.putNumber("port5", pdp.getCurrent(5));
		SmartDashboard.putNumber("port10", pdp.getCurrent(10));
		SmartDashboard.putNumber("port11", pdp.getCurrent(11));
		SmartDashboard.putNumber("port12", pdp.getCurrent(12));
		SmartDashboard.putNumber("port13", pdp.getCurrent(13));
		SmartDashboard.putNumber("port14", pdp.getCurrent(14));
		SmartDashboard.putNumber("port15", pdp.getCurrent(15));


		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

