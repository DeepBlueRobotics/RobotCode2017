package org.usfirst.frc199.Robot2017.subsystems;

import org.usfirst.frc199.Robot2017.DashboardInterface;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.RobotMap;
import org.usfirst.frc199.Robot2017.commands.*;
import org.usfirst.frc199.Robot2017.motion.PID;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem implements ShooterInterface {
	double targetAngle = Robot.getPref("targetAngle", 0); // angle that the ball
															// hits the boiler
	double height = Robot.getPref("relativeHeightOfBoiler", 0);
	double encoderAngleRatio = Robot.getPref("encoderAngleRatio", 0);

	private final double gravity = 9.81;
	private final double mass = 0.074;
	private final double drag = 0.45;
	private final double ro = 1.225;
	private final double r = 0.0635;
	private final double k = drag * ro * Math.PI * r * r / 2;
	private final double thetaErr = 0.05; // CHANGE LATER
	private final double velErr = 0.05; // CHANGE LATER
	
	private final double turretDiam = 7.71;
	
	private final SpeedController shootMotor = RobotMap.shooterShootMotor;
	private final SpeedController feedMotor = RobotMap.shooterFeedMotor;
	private final Encoder shootEncoder = RobotMap.shooterShootEncoder;
	private final CANTalon shootMotorAndEnc = RobotMap.shooterShootMotorAndEncoder;
	private final SpeedController turretMotor = RobotMap.turretTurnMotor;
	private final Encoder turretEncoder = RobotMap.turretTurretEncoder;
	private final Servo hoodServo = RobotMap.hoodServo;

	private PID ShooterPID = new PID("ShooterPID");
	private PID TurretPID = new PID("TurretPID");

	private double prevShooterEncoder = 0;
	private boolean shooterMotorStalled = false;

	private double prevTurretEncoder = 0;
	private double prevHoodEncoder = 0;

	double[][] distances = new double[48][45];
	
	public Shooter() {
		super();
		putString("~TYPE~", "Shooter");
		for (double v = 0; v < 24; v += 0.5) {
			for (double theta = 0; theta < 90; theta += 2) {
				double[][] traj = new double[500][4];
				traj[0][0] = v * Math.cos(theta * Math.PI / 180);
				traj[0][1] = v * Math.sin(theta * Math.PI / 180);
				traj[0][2] = 0;
				traj[0][3] = 0.6096;
				for (int i = 1; i < 500; i++) {
					double vxprev = traj[i - 1][0];
					double vyprev = traj[i - 1][1];
					double xprev = traj[i - 1][2];
					double yprev = traj[i - 1][3];

					double vx = -k * vxprev
							* Math.sqrt(vxprev * vxprev + vyprev * vyprev) * 0.01 / mass
							+ vxprev;
					double vy = (-k * vyprev
							* Math.sqrt(vxprev * vxprev + vyprev * vyprev) / mass
							- gravity) * 0.01 + vyprev;
					double x = vxprev * 0.01 + xprev;
					double y = vyprev * 0.01 + yprev;
					
					traj[i][0] = vx;
					traj[i][1] = vy;
					traj[i][2] = x;
					traj[i][3] = y;
					
					if (y < 2.4638 && vy < 0) {
						distances[(int) v * 2][(int) theta / 2] = x - (2.4638 - y) * (x - xprev) / (yprev - y);
						break;
					}
				}
			}
		}
	}

	public void initDefaultCommand() {
	}

	/**
	 * Sets the feeder motor's speed (from -1.0 to 1.0).
	 * 
	 * @param rate - speed to give the feeder motor
	 */
	public void runFeederMotor(double speed) {
		feedMotor.set(speed);
	}

	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0). For PID AutoShoot
	 * (AutoShoot2)
	 * 
	 * @param rate - speed to give the shooter motor
	 */
	public void runShootMotor2(double speed) {
		shootMotor.set(speed);
	}

	/**
	 * Sets the shooter motor's speed (from -1.0 to 1.0). For CANTalon AutoShoot
	 * 
	 * @param rate - speed to give the shooter motor
	 */
	public void runShootMotor(double speed) {
		double targetSpeed = speed;
		shootMotorAndEnc.setP(1 / targetSpeed);
		shootMotorAndEnc.set(targetSpeed);
	}

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works For PID AutoShoot (AutoShoot2)
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled2() {

		if (Math.abs(shootMotor.get()) >= 0.2
				&& (shootEncoder.get() - prevShooterEncoder) <= Robot.getPref("encoderOffset", 5)
				&& Robot.getPref("shooterEncoderWorks", 0) == 1) {
			shootMotor.set(0);
			System.out.println("Shooter Motor stalled, stopping motor.");
			prevShooterEncoder = shootEncoder.get();
			shooterMotorStalled = true;
		} else {
			prevShooterEncoder = shootEncoder.get();
			shooterMotorStalled = false;
		}
		return shooterMotorStalled;
	}

	/**
	 * This method checks if 1)shootMotor is set to a high value 2)encoder is
	 * saying the shooter isn't moving 3)getPref is saying the shooterEncoder
	 * works For CANTalon AutoShoot
	 * 
	 * @return return whether shooter motor is deemed stalled, however, if it is
	 *         it automatically sets it to 0
	 */
	public boolean shooterMotorStalled() {
		if (Math.abs(shootMotorAndEnc.get()) >= 0.2
				&& (shootMotorAndEnc.getEncPosition() - prevShooterEncoder) <= Robot.getPref("encoderOffset", 5)
				&& Robot.getPref("shooterEncoderWorks", 0) == 1) {
			shootMotorAndEnc.set(0);
			System.out.println("Shooter Motor stalled, stopping motor.");
			shooterMotorStalled = true;
		} else {
			shooterMotorStalled = false;
		}
		prevShooterEncoder = shootMotorAndEnc.getEncPosition();
		return shooterMotorStalled;
	}

	/**
	 * Returns the current speed of the shooter wheel. For PID AutoShoot
	 * (AutoShoot2)
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed2() {
		return shootEncoder.getRate();
	}

	/**
	 * Returns the current speed of the shooter wheel. For CANTalon AutoShoot
	 * 
	 * @return shooter speed in inches per second
	 */
	public double getShooterSpeed() {
		return shootMotorAndEnc.getEncVelocity();
	}

	/**
	 * Tells the shooter motor's PID the target speed to reach.
	 * 
	 * @param targetRate - target speed for shooter motor PID
	 */
	public void setShooterPIDTarget(double targetRate) {
		ShooterPID.setTarget(targetRate);
	}

	/**
	 * Gets the speed for the shooter motor from the shooter PID.
	 * 
	 * @return speed for motor
	 */
	public double getShooterPIDOutput() {
		return ShooterPID.getOutput();
	}

	/**
	 * Updates the shooter motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue current shooter motor encoder speed
	 */
	public void updateShooterPID(double updateValue) {
		ShooterPID.update(updateValue);
	}

	/**
	 * Stops the shooter motor
	 */
	public void stopShootMotor() {
		runShootMotor(0);
	}

	/**
	 * Used only in TestPID
	 * 
	 * @param target - the target value for PID
	 * @return speed of motor
	 */
	public double updateSpeed(double target) {
		ShooterPID.setTarget(target);
		ShooterPID.update(getShooterSpeed());
		return ShooterPID.getOutput();
	}

	/**
	 * Passes the distance of the robot from the boiler through an equation to
	 * compute the speed at which we should be shooting.
	 * 
	 * @param distance
	 *            - in meters of the front of the robot from the boiler
	 * @return the ideal exit speed of the ball in meters per second and the ideal angle in degrees in an array
	 */
	public double[] convertDistanceToTargetVelocityAndAngle(double distance) {
		double minErr = 100;
		double velocity = 0;
		double angle = 0;
		for (int j = 1; j < 44; j++) {
			for (int i = 0; i < 47; i++) {
				double d = distances[i + 1][j] - distance;
				double totd = distances[i + 1][j] - distances[i][j];
				if (distances[i][j] < distance && distances[i + 1][j] > distance) {
					// velError = velocity * velErr * change in distance per velocity 
					double velError = (((i + 1)/ 2) - 0.5 * d / totd) * velErr * (distances[i + 1][j] - distances[i][j]); 
					// thetaError = theta * thetaErr * change in distance per theta
					double thetaError = (j * 2) * thetaErr * (distances[i][j + 1] - distances[i][j - 1]) / 4;
					if (minErr > velError + thetaError) {
						minErr = velError + thetaError;
						velocity = i / 2;
						angle = j * 2;
					}
				}
			}
		}
		double[] result = new double[2];
		result[0] = velocity;
		result[1] = angle;
		return result;
	}

	// come up with PID methods (for turret) similar to those of
	// shooter
	// turret should use vision
	// hood should convert real angles to encoder values (test to find a ratio)
	
	/**
	 * Used for turret only
	 * @param angle - in degrees
	 * @return the target distance in inches
	 * */
	public double convertAngleToTargetDistance(double angle){
		//return (turretDiam / 2) * angle * Math.PI / 180;
		return angle * 18 / 3 * 256;
	}
	
	/**
	 * Resets turret encoder
	 * Sets distPerPulse for turret encoder???
	 * */
	public void resetTurretEncoder(){
		turretEncoder.reset();
	}

	/**
	 * Gets the turret encoder value
	 * 
	 * @return the turret encoder value
	 */
	public double getTurretEncoder() {
		return turretEncoder.getDistance();
	}

	/**
	 * Sets the turret motor's speed (from -1.0 to 1.0).
	 *  
	 * @param rate
	 *            - speed to give the turret motor
	 * 
	 * @param rate - speed to give the turret motor
	 */
	public void runTurretMotor(double speed) {
		turretMotor.set(speed);
	}

	/**
	 * Tells the turret motor's PID the target speed to reach.
	 * 
	 * @param targetRate - target speed for turret motor PID
	 */
	public void setTurretPIDTarget(double target) {
		TurretPID.setTarget(target);
	}

	/**
	 * Gets the speed for the turret motor from the turret PID.
	 * 
	 * @return speed for motor
	 */
	public double getTurretPIDOutput() {
		return TurretPID.getOutput();
	}

	/**
	 * Updates the turret motor PID with the current speed from the encoder.
	 * 
	 * @param updateValue current turret motor encoder speed
	 */
	public void updateTurretPID(double updateValue) {
		TurretPID.update(updateValue);
	}

	/**
	 * Gets if turret PID target reached or not.
	 * 
	 * @return turret PID target is reached or not
	 */
	public boolean turretPIDTargetReached() {
		return TurretPID.reachedTarget();
	}

	/**
	 * Stops the turret motor
	 */
	public void stopTurretMotor() {
		runTurretMotor(0);
	}

	/**
	 * Gets the hood servo value
	 * 
	 * @return the hood servo value
	 */
	public double getHoodServo() {
		return hoodServo.get();
	}

	/**
	 * Sets the hood servo's position (from 0.0 to 1.0).
	 * 
	 * @param position - position to give the hood servo
	 */
	public void setHoodServo(double position) {
		hoodServo.set(position);
	}

	/**
	 * Sets the hood angle in radians using the hood servo. Uses math to turn
	 * that angle into an argument for setHoodServo().
	 * 
	 * @param targetShootAngle - angle from ground for the ball to exit to give
	 *            the hood
	 */
	public void setHoodAngle(double targetShootAngle) {
		// all distances are in inches
		// actuator measurements
		double closedLength = 4.64567;
		double stroke = 1.9685;
	
		// all angles are in radians
		//
		// components of pistonDistance
		double pistonBottomX = 2.930586;
		double pistonBottomY = 2.1351762;
		// target angle for angle piston/hood-hood pivot-piston-body
		double targetPistonAngle = Math.atan(pistonBottomY / pistonBottomX) - targetShootAngle + Math.PI / 2;
		// distance from hood-body pivot to piston-hood pivot
		double hoodHeight = Math.sqrt(pistonBottomX * pistonBottomX + pistonBottomY * pistonBottomY);
		// distance from hood-body pivot to piston-body pivot
		double pistonDistance = 3.6258839;
		// length the piston should be, from piston-body piston to piston-hood
		// piston
		double pistonHeight = Math.sqrt(
				// ( a^2 + b^2
				hoodHeight * hoodHeight + pistonDistance * pistonDistance
				// - 2ab*cos(C) )
						- 2 * hoodHeight * pistonDistance * Math.cos(targetPistonAngle));
		// the value (0.0 to 1.0) to pass to .set()
		double pistonArgument = (pistonHeight - closedLength) / stroke;


		setHoodServo(pistonArgument);
	}

	@Override
	public void displayData() {
		putBoolean("Shooter motor stalled", shooterMotorStalled);
		// putNumber("Shoot encoder rate", shootEncoder.getRate());
		putNumber("Shooter PID output", getShooterPIDOutput());
		putNumber("Turret encoder value", turretEncoder.get());
		putNumber("Hood servo value", hoodServo.get());
	}
}
