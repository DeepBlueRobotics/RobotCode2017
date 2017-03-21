package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem implements DashboardInterface {
	// inches between the centers of both reflectors (horizontal)
	private final double REFLECTOR_DIST_GEAR = 8.5; //8.5
	//degrees of offset of gear cam if not facing straight forward
	private final double GEAR_CAM_OFFSET = Robot.getPref("Gear cam angle offset", 0);
	// inches off the ground (~21.67)
	private final double SHOOTER_CAM_HEIGHT = Robot.getPref("Camera Height", 0); 
	// in from camera bottom to top of boiler tape
	private final double BOILER_HEIGHT = 88 - SHOOTER_CAM_HEIGHT; 
	// radians from north that you can see in both directions
	private final double THETA = Math.toRadians(29.85);
	// radians of the camera's vertical field of view
	private final double SHOOTER_CAM_ANGLE = Math.toRadians(35.78);
	// pixel width of image
	private final double RESOLUTION_WIDTH = 640; 
	// pixel height of image
	private final double RESOLUTION_HEIGHT = 360;
	private double SCREEN_DEPTH = RESOLUTION_HEIGHT / Math.tan(SHOOTER_CAM_ANGLE);
	private final double SCREEN_CENTER_X = RESOLUTION_WIDTH / 2;
	private final double SCREEN_CENTER_Y = RESOLUTION_HEIGHT / 2;

 	private final double PIVOT_TO_FRONT_DISTANCE = Robot.getPref("Distance from pivot point to front of robot", 0);

 	private final String[] gearVisionKeys = {
 		"leftGearCenterX", "leftGearCenterY", "rightGearCenterX", "rightGearCenterY", 
 		"leftGearBottomY", "leftGearTopY", "rightGearBottomY", "rightGearTopY"
 	};

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
  			double leftGearCenterY = getNumber("leftGearCenterY", 0);
 			double rightGearCenterY = getNumber("rightGearCenterY", 0);
			double pixelDist = Math.sqrt(Math.pow(rightGearCenterX - leftGearCenterX, 2) + Math.pow(rightGearCenterY - leftGearCenterY, 2));
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
  			double m = (getNumber("rightGearCenterY", 0) - getNumber("leftGearCenterY", 0)) / 
  					(getNumber("rightGearCenterX", 0) - getNumber("leftGearCenterX", 0));
  	
			return ( pegX + m*pegY - m*SCREEN_CENTER_Y + SCREEN_CENTER_X ) / (m*m + 1);
  		} else {
  			return 0;
  		}
 	}
 
 	/**
 	 * @return calculated angle from camera's line of sight to gear lift
 	 */
 	public double getCameraAngleToGear() {
 //		double pixelDisplacement = getPixelDistanceToGear();
 
 		double pegX = (getNumber("leftGearCenterX", 0) + getNumber("rightGearCenterX", 0)) / 2;
 		double pixelDisplacement = pegX - SCREEN_CENTER_X;
 		double abstractDepth = (RESOLUTION_WIDTH / 2) / Math.tan(THETA);
 		
 		return Math.atan(pixelDisplacement / abstractDepth);
 	}
 	
 	/**
 	 * @return distance from front of robot to gear lift in inches
 	 */
 	public double getDistanceToGear() {

 		if (getBoolean("OH-YEAH", true)) {
	 		double l = getCameraDistanceToGearPlane();
	 		double theta = getCameraAngleToGear() + Math.toRadians(GEAR_CAM_OFFSET);
	 		double x = Robot.getPref("Gear cam x distance from pivot", 0);
	 		double y = Robot.getPref("Gear cam y distance from pivot", 0);
	 		double r = Math.sqrt( x*x + y*y);
	 		double psi = Math.atan(x/y);
	 		double d = l / Math.cos(theta);
	 		double beta = Math.PI - theta - psi;
	 		
	 		return Math.sqrt(r*r + d*d - 2*r*d*Math.cos(beta)) - PIVOT_TO_FRONT_DISTANCE;

  		} else {
  			return 0;
  		}
 	}
 	
 	/**
 	 * @return robot's angle to gear lift in degrees
 	 */
 	public double getAngleToGear() {
 		if (getBoolean("OH-YEAH", true)) {
	 		double l = getCameraDistanceToGearPlane() + Math.toRadians(GEAR_CAM_OFFSET);
	 		double theta = getCameraAngleToGear();
	 		double x = Robot.getPref("Gear cam x distance from pivot", 0);
	 		double y = Robot.getPref("Gear cam y distance from pivot", 0);
	 		double r = Math.sqrt( x*x + y*y);
	 		double psi = Math.atan(x/y);
	 		double d = l / Math.cos(theta);
	 		double beta = Math.PI - theta - psi;
	 		double D = Math.sqrt( r*r + d*d - 2*r*d*Math.cos(beta));
	 		
	 		return Math.toDegrees(Math.asin(d*Math.sin(beta)/D) - psi);

		} else {
			return 0;
		}
 	}
	
	public double getParallacticDistanceToGear() {
		double d1 = getDistanceToSingleGearMark(getNumber("rightGearBottomY", 0), getNumber("rightGearTopY", 0) );
		double d2 = getDistanceToSingleGearMark(getNumber("leftGearBottomY", 0), getNumber("leftGearTopY", 0) );
		double dBetween = REFLECTOR_DIST_GEAR;
		// From https://en.wikipedia.org/wiki/Median_(geometry)#Formulas_involving_the_medians.27_lengths
		return Math.sqrt(2*d1*d1+2*d2*d2-dBetween*dBetween)/2;
	}
	
	
	public double getDistanceToSingleGearMark(double yLow, double yHigh) {
		double pixelDist = Math.abs(yLow - yHigh);
		double fieldOfView = (REFLECTOR_DIST_GEAR * RESOLUTION_WIDTH) / pixelDist;
		double distanceToMark = (fieldOfView / 2) / (Math.tan(THETA));
		return distanceToMark;
	}
	
	public boolean alignedButHorizontallyOffset() {
		return ( Math.abs(getAngleToGear()) < 0.6 ) && 
				(Math.abs(getParallacticDistanceToGear()) > Math.abs(getCameraDistanceToGearPlane() + 1) );
	}
	
	public double angleToTurnIfHorizontallyOffset() {
		double parDist = getParallacticDistanceToGear();
		double camDist = getCameraDistanceToGearPlane();
		
		double theta =  Math.atan(parDist / (2*Math.sqrt(parDist*parDist - camDist*camDist)));
		return Math.toDegrees(directionToTurnIfHorizontallyOffset() * (Math.PI/2 - theta));
	}
	
	public double angleToTurnBackIfHorizontallyOffset() {
		double parDist = getParallacticDistanceToGear();
		double camDist = getCameraDistanceToGearPlane();
		double theta =  Math.toRadians( Math.atan(parDist / ( 2*Math.sqrt(parDist*parDist - camDist*camDist) )) );
		double angle = -directionToTurnIfHorizontallyOffset() * (theta + 
				Math.acos(getCameraDistanceToGearPlane()/getParallacticDistanceToGear()));
		
		return Math.toDegrees(angle);
	}
	
	public double distanceToTravelIfHorizontallyOffset() {
		double parDist = getParallacticDistanceToGear();
		double camDist = getCameraDistanceToGearPlane();
		
		double theta =  Math.atan(parDist / (2*Math.sqrt(parDist*parDist - camDist*camDist)));
		return getParallacticDistanceToGear() / (2*Math.sin(theta));
	}
	
	public double directionToTurnIfHorizontallyOffset() {
		double d1 = getDistanceToSingleGearMark(getNumber("rightGearBottomY", 0), getNumber("rightGearTopY", 0) );
		double d2 = getDistanceToSingleGearMark(getNumber("leftGearBottomY", 0), getNumber("leftGearTopY", 0) );
		
		if (d1 > d2) return 1;
		else return -1;
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
		putNumber("Gear camera angle to target", getCameraAngleToGear());
		putBoolean("Is aligned but horizontally offset", alignedButHorizontallyOffset());
		putNumber("Angle to turn if horizontally offset", angleToTurnIfHorizontallyOffset());
		

	}
}
