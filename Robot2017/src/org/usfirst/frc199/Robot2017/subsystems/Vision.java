package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * TODO: (Ana T.) Write all methods that process target info passed to the RoboRIO in here
 */
public class Vision extends Subsystem implements DashboardInterface {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private final double REFLECTOR_DIST_GEAR = 5; //inches between the centers of both reflectors (horizontal)
	private final double REFLECTOR_DIST_BOILER = 7; //inches between the centers of both reflectors (vertical)
	private final double THETA = 37.5 * (Math.PI/180); //radians from north that you can see in both directions
	private final double RESOLUTION_WIDTH = 1280; //pixel width of image
	private final double SCREEN_CENTER = RESOLUTION_WIDTH / 2;
	
	
    public void initDefaultCommand() {

    }

    public double getDistanceToGear()
    {
    	double leftGearCenterX =  getNumber("Vision/leftGearCenterX", 0);
    	double rightGearCenterX = getNumber("Vision/rightGearCenterX", 0);
    	double pixelDist = Math.abs(rightGearCenterX - leftGearCenterX);
    	double fieldOfView = (REFLECTOR_DIST_GEAR * RESOLUTION_WIDTH) / pixelDist;
    	double distanceToGear = (fieldOfView / 2) / (Math.tan(THETA));
    	return distanceToGear;
    }
    
    public double getAngleToGear()
    {	
    	double pegX = (getNumber("Vision/leftGearCenterX", 0) + getNumber("Vision/rightGearCenterX", 0))/2;
    	double pixelDisplacement = SCREEN_CENTER - pegX;
    	double abstractDepth = (RESOLUTION_WIDTH/2)/Math.tan(THETA);
    	
    	double angle = (Math.atan(pixelDisplacement/abstractDepth) * 180) / Math.PI;
    	
    	return angle;
    }
    
    public double getDistanceToBoiler()
    {
    	double topBoilerCenterY =  getNumber("Vision/topBoilerCenterY", 0);
    	double bottomBoilerCenterY = getNumber("Vision/bottomBoilerCenterY", 0);
    	double pixelDist = Math.abs(topBoilerCenterY - bottomBoilerCenterY);
    	double fieldOfView = (REFLECTOR_DIST_BOILER * RESOLUTION_WIDTH) / pixelDist;
    	double distanceToBoiler = (fieldOfView / 2) / (Math.tan(THETA));
    	return distanceToBoiler;
    }
    
    public double getAngleToBoiler()
    {	
    	double tapeCenterX = getNumber("Vision/topBoilerCenterX", 0);
    	double pixelDisplacement = SCREEN_CENTER - tapeCenterX;
    	double abstractDepth = (RESOLUTION_WIDTH/2)/Math.tan(THETA);
    	
    	double angle = (Math.atan(pixelDisplacement/abstractDepth) * 180) / Math.PI;
    	
    	return angle;
    }
    
	@Override
	public void displayData() {
		putNumber("Distance to Gear", getDistanceToGear());
		putNumber("Angle to Gear", getAngleToGear());
		putNumber("Distance to Boiler", getDistanceToBoiler());
		putNumber("Angle to Boiler", getAngleToBoiler());
		
	}
    
    
}

