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
	private final double REFLECTOR_DIST_GEAR = 5; //inches between the centers of both gear reflectors
	private final double REFLECTOR_DIST_BOILER = 7; //inches between the centers of both boiler reflectors
	private final double THETA = 37.5 * (Math.PI/180); //degrees from north that you can see in both directions
	private final double RESOLUTION_WIDTH = 1280; //pixel width of image
	private final double SCREEN_CENTER = RESOLUTION_WIDTH / 2;
	
	
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    
    public double getDistanceToGear()
    {
    	double distanceToGear = (getFieldOfViewGear() / 2) / (Math.tan(THETA));
    	return distanceToGear;
    }
    
    public double getAngleToGear()
    {	
    	double pegX = (getNumber("leftGearCenterX", 0) + getNumber("rightGearCenterX", 0))/2;
    	double pixelDisplacement = Math.abs(SCREEN_CENTER - pegX);
    	double displacement = (getFieldOfViewGear() * pixelDisplacement) / RESOLUTION_WIDTH;
    	double angle = (Math.atan(displacement/getDistanceToGear()) * 180) / Math.PI;
    	
    	return angle;
    }
    
    public double getFieldOfViewGear()
    {
    	double leftGearCenterX =  getNumber("leftGearCenterX", 0);
    	double rightGearCenterX = getNumber("rightGearCenterX", 0);
    	double pixelDist = Math.abs(rightGearCenterX - leftGearCenterX);
    	double fieldOfView = (REFLECTOR_DIST_GEAR * RESOLUTION_WIDTH) / pixelDist;
    	return fieldOfView;
    }
    
    public double getDistanceToBoiler()
    {
    	double distanceToBoiler = (getFieldOfViewBoiler() / 2) / (Math.tan(THETA));
    	return distanceToBoiler;
    }
    
    public double getAngleToBoiler()
    {
    	double CenterX = (getNumber("topBoilerCenterX", 0));
    	double pixelDisplacement = Math.abs(SCREEN_CENTER - CenterX);
    	double displacement = (getFieldOfViewBoiler() * pixelDisplacement) / RESOLUTION_WIDTH;
    	double angle = (Math.atan(displacement/getDistanceToBoiler()) * 180) / Math.PI;
    	
    	return angle;
    }
    
    public double getFieldOfViewBoiler()
    {
    	double topBoilerCenterY = getNumber("topBoilerCenterY",0);
    	double bottomBoilerCenterY = getNumber("bottomBoilerCenterY",0);
    	double pixelDist = Math.abs(topBoilerCenterY-bottomBoilerCenterY);
    	double fieldOfView = (REFLECTOR_DIST_BOILER * RESOLUTION_WIDTH) / pixelDist;
    	return fieldOfView;
    }
    
	@Override
	public void displayData() {
		putNumber("Angle to gear", getAngleToGear());
		putNumber("Distance to gear", getDistanceToGear());
		putNumber("Angle to boiler", getAngleToBoiler());
		putNumber("Distance to boiler", getDistanceToBoiler());
		
	}
    
    
}

