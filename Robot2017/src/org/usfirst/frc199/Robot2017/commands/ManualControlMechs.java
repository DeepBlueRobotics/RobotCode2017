package org.usfirst.frc199.Robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ClimberInterface;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

/**
 *
 */
public class ManualControlMechs extends Command {
	private ClimberInterface climber;
	private ShooterInterface shooter;
	private IntakeInterface intake;
	
	
	public ManualControlMechs(IntakeInterface intake, ShooterInterface shooter, ClimberInterface climber) {
		this.shooter = shooter;
		this.climber = climber;
		this.intake = intake;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	
	
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	String system = SmartDashboard.getString("ManualControl/Manual Control Mech");
    	manualSwitch(system, Robot.oi.manipulator.getThrottle());
    }
    
    public void manualSwitch(String system, double speed) {
    	switch (system.toLowerCase()){
	    	case "intake": this.intake.runIntake(speed);
	    		break;
	    	case "feeder": this.shooter.runFeederMotor(speed);
	    		break;
	    	case "climber": this.climber.runClimber(speed);
	    		break;
	    	case "shooter": this.shooter.runShootMotor(speed);
	    		break;
	    	case "turret": this.shooter.runTurretMotor(speed);
	    		break;
    	}
    }
	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		String system = SmartDashboard.getString("ManualControl/Manual Control Mech");
		manualSwitch(system, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		String system = SmartDashboard.getString("ManualControl/Manual Control Mech");
		manualSwitch(system, 0);
	}
}
