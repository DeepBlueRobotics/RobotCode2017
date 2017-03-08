package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * TODO: (Ana T.) Write all methods that process target info passed to the
 * RoboRIO in here
 */
public class Vision extends Subsystem implements DashboardInterface {
	// inches between the centers of both reflectors (horizontal)
	private final double REFLECTOR_DIST_GEAR = 5;
	// inches off the ground (~21.67)
	private final double SHOOTER_CAM_HEIGHT = Robot.getPref("Camera Height", 0); 
	// in from camera bottom to top of boiler tape
	private final double BOILER_HEIGHT = 88 - SHOOTER_CAM_HEIGHT; 
	// radians from north that you can see in both directions
	private final double THETA = 34.25 * (Math.PI / 180);
	// radians of the camera's vertical field of view
	private final double SHOOTER_CAM_ANGLE = 41.91 * (Math.PI / 180);
	// pixel width of image
	private final double RESOLUTION_WIDTH = 640; 
	// pixel height of image
	private final double RESOLUTION_HEIGHT = 360;
	private double SCREEN_DEPTH = RESOLUTION_HEIGHT / Math.tan(SHOOTER_CAM_ANGLE);
	private final double SCREEN_CENTER = RESOLUTION_WIDTH / 2;

	public Vision() {
		super();
		putString("~TYPE~", "Vision");
	}

	public void initDefaultCommand() {

	}

	public double getDistanceToGear() {
		if (getBoolean("OH-YEAH", true)) {
			double leftGearCenterX = getNumber("leftGearCenterX", 0);
			double rightGearCenterX = getNumber("rightGearCenterX", 0);
			double pixelDist = Math.abs(rightGearCenterX - leftGearCenterX);
			double fieldOfView = (REFLECTOR_DIST_GEAR * RESOLUTION_WIDTH) / pixelDist;
			double distanceToGear = (fieldOfView / 2) / (Math.tan(THETA));
			// not sure why it is neccesary to multiply the distance by two, but the actual
			// distance is always almost exactly twice the distance calculated, so yeah :)
			return 2 * distanceToGear; 
		} else {
			return 0;
		}
	}

	public double getAngleToGear() {
		if (getBoolean("OH-YEAH", true)) {
			double pegX = (getNumber("leftGearCenterX", 0) + getNumber("rightGearCenterX", 0)) / 2;
			// calculates displacement based on the calibrated center gearCenterX
			double pixelDisplacement = pegX - getNumber("gearCenterX"); 
			double abstractDepth = (RESOLUTION_WIDTH / 2) / Math.tan(THETA);

			double angle = (Math.atan(pixelDisplacement / abstractDepth) * 180) / Math.PI;

			return angle;
		} else {
			return 0;
		}
	}

	public double getDistanceToBoiler() {
		if (getBoolean("boilerFound", true)) {
			double pixelHeight = getNumber("boilerY", 0);
			double distanceToBoiler = (SCREEN_DEPTH * BOILER_HEIGHT) / pixelHeight;
			return distanceToBoiler;
		} else {
			return 0;
		}
	}

	public double getAngleToBoiler() {
		if (getBoolean("boilerFound", true)) {
			double tapeCenterX = getNumber("boilerX", 0);
			double pixelDisplacement = tapeCenterX - SCREEN_CENTER;
			double abstractDepth = (RESOLUTION_WIDTH / 2) / Math.tan(THETA);

			double angle = (Math.atan(pixelDisplacement / abstractDepth) * 180) / Math.PI;

			return angle;
		} else {
			return 0;
		}
	}

	@Override
	public void displayData() {
		putNumber("Distance to Gear", getDistanceToGear());
		putNumber("Angle to Gear", getAngleToGear());
		putNumber("Distance to Boiler", getDistanceToBoiler());
		putNumber("Angle to Boiler", getAngleToBoiler());

	}

}
