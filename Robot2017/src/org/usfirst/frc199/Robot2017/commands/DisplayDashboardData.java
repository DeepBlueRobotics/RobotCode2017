package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Run in the main Robot() class to call all displayData() methods in subsystems, 
 * putting all data in Network Tables for the purpose of the omni widget
 * 
 * FUNCITONAL
 */
public class DisplayDashboardData extends Command {

	public DisplayDashboardData() {
	}

	protected void initialize() {
	}

	protected void execute() {
		for (DashboardInterface s : Robot.subsystems) {
			s.displayData();
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
