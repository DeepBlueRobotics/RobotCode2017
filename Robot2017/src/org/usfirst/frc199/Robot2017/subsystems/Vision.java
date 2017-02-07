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
	private final double REFLECTOR_DIST = 5; //inches between the centers of both reflectors
	private final double THETA = 37.5 * (Math.PI/180); //degrees from north that you can see in both directions
	private final double RESOLUTION_WIDTH = 1280; //pixel width of image
	private final double SCREEN_CENTER = RESOLUTION_WIDTH / 2;
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    //TODO: make getAngleToGear not rely on getDistanceToGear, same for boiler
    
    public double getDistanceToGear()
    {
    	double distanceToGear = (getFieldOfView() / 2) / (Math.tan(THETA));
    	return distanceToGear;
    }
    
    public double getAngleToGear()
    {	
    	double pegX = (getNumber("Vision/leftGearCenterX", 0) + getNumber("Vision/rightGearCenterX", 0))/2;
    	double pixelDisplacement = Math.abs(SCREEN_CENTER - pegX);
    	double displacement = (getFieldOfView() * pixelDisplacement) / RESOLUTION_WIDTH;
    	double angle = (Math.atan(displacement/getDistanceToGear()) * 180) / Math.PI;
    	
    	return angle;
    }
    
    public double getFieldOfView()
    {
    	double leftGearCenterX=  getNumber("Vision/leftGearCenterX", 0);
    	double rightGearCenterX = getNumber("Vision/rightGearCenterX", 0);
    	double pixelDist = Math.abs(rightGearCenterX - leftGearCenterX);
    	double fieldOfView = (REFLECTOR_DIST * RESOLUTION_WIDTH) / pixelDist;
    	return fieldOfView;
    }
    
    
    
    
    
	@Override
	public void displayData() {
		// TODO Auto-generated method stub
		
	}
    
    
}

