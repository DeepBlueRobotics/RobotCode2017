package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

/**
 *
 */
public class TurnToWaypoint extends TurnToHeading {

	public TurnToWaypoint(WaypointAndHeading w, DrivetrainInterface drivetrain) {
		super(w,drivetrain);
		this.targetAngle = w.headingToWaypoint;
		
	}

	// Called just before this Command runs the first time
	public void initialize() {
		
		drivetrain.resetGyro();
		super.initialize();

	}
}
