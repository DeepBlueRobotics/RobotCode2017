package org.usfirst.frc199.Robot2017.commands;

import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.smartdashboard.SmartDashboard;

public class WriteToNT extends Command {

	private String key;
	private Object value;
	
    public WriteToNT(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (value instanceof String) {
            SmartDashboard.putString(key, (String) value);
        } else if (value instanceof Double) {
            SmartDashboard.putNumber(key, (double) value);
        } else if (value instanceof Integer) {
            SmartDashboard.putNumber(key, ((Integer) value).doubleValue());
        } else if (value instanceof Boolean) {
            SmartDashboard.putBoolean(key, (boolean) value);
        } else if (value instanceof Sendable) {
            SmartDashboard.putData(key, (Sendable) value);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    public void end() {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
