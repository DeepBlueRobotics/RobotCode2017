package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem implements DashboardInterface {
	// inches between the centers of both reflectors (horizontal)
	private final double REFLECTOR_DIST_GEAR = 5;
	// inches off the ground (~21.67)
	private final double SHOOTER_CAM_HEIGHT = Robot.getPref("Camera Height", 0); 
	// in from camera bottom to top of boiler tape
	private final double BOILER_HEIGHT = 88 - SHOOTER_CAM_HEIGHT; 
	// radians from north that you can see in both directions
	private final double THETA = Math.toRadians(34.25);
	// radians of the camera's vertical field of view
	private final double SHOOTER_CAM_ANGLE = Math.toRadians(41.91);
	// pixel width of image
	private final double RESOLUTION_WIDTH = 640; 
	// pixel height of image
	private final double RESOLUTION_HEIGHT = 360;
	private double SCREEN_DEPTH = RESOLUTION_HEIGHT / Math.tan(SHOOTER_CAM_ANGLE);
	private final double SCREEN_CENTER_X = RESOLUTION_WIDTH / 2;
	private final double SCREEN_CENTER_Y = RESOLUTION_HEIGHT / 2;
	
	private final double GEAR_CAM_X_FROM_PIVOT = Robot.getPref("Gear cam x distance from pivot", 0);
	private final double GEAR_CAM_Y_FROM_PIVOT = Robot.getPref("Gear cam y distance from pivot", 0);
	private final double PIVOT_TO_FRONT_DISTANCE = Robot.getPref("Distance from pivot point to front of robot", 0);

	public Vision() {
		super();
		putString("~TYPE~", "Vision");
	}

	public void initDefaultCommand() {

	}
	
	/**
	 * @return distance from camera to the plane perpendicular to its line of sight, on which the gear lift lies
	 */
	public double getCameraDistanceToGearPlane() {
		if (getBoolean("OH-YEAH", true)) {
			double leftGearCenterX = getNumber("leftGearCenterX", 0);
			double rightGearCenterX = getNumber("rightGearCenterX", 0);
			double pixelDist = Math.abs(rightGearCenterX - leftGearCenterX);
			double fieldOfView = (REFLECTOR_DIST_GEAR * RESOLUTION_WIDTH) / pixelDist;
			double distanceToGear = (fieldOfView / 2) / (Math.tan(THETA));
			return distanceToGear; 
		} else {
			return 0;
		}
	}
	
	/**
	 * Accounts for camera tilt in order to calculate distance or angle to gear lift
	 * @return distance in pixel's from relative center of screen
	 */
	public double getPixelDistanceToGear() {
		if (getBoolean("OH-YEAH", true)) {
			double pegX = (getNumber("leftGearCenterX", 0) + getNumber("rightGearCenterX", 0)) / 2;
			double pegY = (getNumber("leftGearCenterY", 0) + getNumber("rightGearCenterY", 0)) / 2;
			double m = (getNumber("leftGearCenterY", 0) - getNumber("rightGearCenterY", 0)) / 
					(getNumber("leftGearCenterX", 0) - getNumber("rightGearCenterX", 0));
	
			return Math.abs(pegX + m*pegY - m*SCREEN_CENTER_Y - SCREEN_CENTER_X) / Math.sqrt(m*m + 1);
		} else {
			return 0;
		}
	}

	/**
	 * @return calculated angle from camera's line of sight to gear lift
	 */
	public double getCameraAngleToGear() {
		double pixelDisplacement = getPixelDistanceToGear();
		double abstractDepth = (RESOLUTION_WIDTH / 2) / Math.tan(THETA);
		
		return Math.atan(pixelDisplacement / abstractDepth);
	}
	
	/**
	 * @return distance from front of robot to gear lift in inches
	 */
	public double getDistanceToGear() {
		double l = getCameraDistanceToGearPlane();
		double theta = getCameraAngleToGear();
		double x = GEAR_CAM_X_FROM_PIVOT;
		double y = GEAR_CAM_Y_FROM_PIVOT;
		double r = Math.sqrt( x*x + y*y);
		double psi = Math.atan(x/y);
		double d = l / Math.cos(theta);
		double beta = Math.PI*3/4 - theta - psi;
		
		return Math.sqrt(r*r + d*d - 2*r*d*Math.cos(beta)) - PIVOT_TO_FRONT_DISTANCE;
	}
	
	/**
	 * @return robot's angle to gear lift in degrees
	 */
	public double getAngleToGear() {
		double l = getCameraDistanceToGearPlane();
		double theta = getCameraAngleToGear();
		double x = GEAR_CAM_X_FROM_PIVOT;
		double y = GEAR_CAM_Y_FROM_PIVOT;
		double r = Math.sqrt( x*x + y*y);
		double psi = Math.atan(x/y);
		double d = l / Math.cos(theta);
		double beta = Math.PI*3/4 - theta - psi;
		double D = r*r + d*d - 2*r*d*Math.cos(beta);
		
		return Math.toDegrees(Math.asin(d*Math.sin(beta)/D) - psi);
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
			double pixelDisplacement = tapeCenterX - SCREEN_CENTER_X;
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
