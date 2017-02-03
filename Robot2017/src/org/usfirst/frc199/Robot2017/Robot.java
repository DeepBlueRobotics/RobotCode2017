package org.usfirst.frc199.Robot2017;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.commands.*;
import org.usfirst.frc199.Robot2017.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Command autonomousCommand;

	public static OI oi;
	public static ArrayList<DashboardInterface> subsystems = new ArrayList<>();
	public static Drivetrain drivetrain;
	public static Intake intake;
	public static Shooter shooter;
	public static Climber climber;
	public static Vision vision;
	


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		RobotMap.init();
		drivetrain = new Drivetrain();
		intake = new Intake();
		shooter = new Shooter();
		climber = new Climber();
		vision = new Vision();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		
		subsystems.add(drivetrain);
		subsystems.add(intake);
		subsystems.add(shooter);
		subsystems.add(climber);
		subsystems.add(vision);
		oi = new OI();
		for(DashboardInterface s: Robot.subsystems) {
    		if(!s.getKey("").substring(0, 4).equals("PID/")) {
    			s.putString("~TYPE~", "SubSystem");
    		}
    	}

		// instantiate the command used for the autonomous period
		autonomousCommand = new MainAutoMode();

	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
		new DisplayDashboardData().start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		new DisplayDashboardData().start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("gyro reading", RobotMap.ahrs.getAngle());
		SmartDashboard.putString("shift piston", RobotMap.drivetrainShiftPiston.get().toString());
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

	/**
	 * Gets a preference or creates one if it does not yet exist
	 * 
	 * @param key
	 *            - The name of the preference
	 * @param defaultValue
	 *            - Backup value if the preference is not found
	 * @return The value of the preference
	 */
	public static double getPref(String key, double defaultValue) {
		if (!Preferences.getInstance().containsKey(key)) {
			Preferences.getInstance().putDouble(key, defaultValue);
		}
		return Preferences.getInstance().getDouble(key, defaultValue);
	}
}
