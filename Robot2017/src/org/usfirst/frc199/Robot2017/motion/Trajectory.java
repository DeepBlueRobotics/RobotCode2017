package org.usfirst.frc199.Robot2017.motion;

import org.usfirst.frc199.Robot2017.Robot;

/**
 * Generates velocity values for a set of points along a path.
 */
public class Trajectory {

	// The path used to generate the trajectory
	private final Path path;
	// The constraints on the trajectory
	private final double vmax, amax, wmax, alphamax;
	// The number of points
	private final int points;
	// The set of calculated target velocity values
	private final double[] velocities;

	/**
	 * Generates a trajectory that follows a given path under the given
	 * constraints
	 * 
	 * @param path
	 *            - the path to follow
	 * @param v0
	 *            - the initial velocity
	 * @param v1
	 *            - the final velocity
	 * @param vmax
	 *            - the absolute maximum allowed velocity
	 * @param amax
	 *            - the absolute maximum allowed acceleration
	 * @param wmax
	 *            - the absolute maximum allowed angular velocity
	 * @param alphamax
	 *            - the absolute maximum allowed angular acceleration
	 * @param points
	 *            - the number of points to sample along the trajectory
	 */
	public Trajectory(Path path, double v0, double v1, double vmax, double amax, double wmax, double alphamax,
			int points) {
		this.path = path;
		this.vmax = vmax;
		this.amax = amax;
		this.wmax = wmax;
		this.alphamax = alphamax;
		this.points = points;

		velocities = new double[points];
		velocities[0] = v0;
		velocities[points - 1] = v1;

		computeForwardTrajectory();
		computeReverseTrajectory();
		for (int i = 0; i < points; i++) {
			System.out.println(velocities[i]);
		}
	}

	/**
	 * Generates a trajectory that follows a given path under default
	 * constraints
	 * 
	 * @param path
	 *            - the path to follow
	 * @param v0
	 *            - the initial velocity
	 * @param v1
	 *            - the final velocity
	 * @param points
	 *            - the number of points to sample along the trajectory
	 */
	public Trajectory(Path path, double v0, double v1, int points) {
		this.path = path;
		this.vmax = Robot.getPref("DriveMaxV", .01);
		this.amax = Robot.getPref("DriveMaxA", .01);
		this.wmax = Robot.getPref("DriveMaxW", .01);
		this.alphamax = Robot.getPref("DriveMaxAlpha", .01);
		this.points = points;
		velocities = new double[points];
		velocities[0] = v0;
		velocities[points - 1] = v1;
		computeForwardTrajectory();
		computeReverseTrajectory();
	}

	/**
	 * Numerically computes the optimal velocity value at each point on the
	 * trajectory
	 */
	private void computeForwardTrajectory() {
		for (int i = 1; i < points - 1; i++) {
			double vprev = velocities[i - 1];
			double vmax = getVmax(i);
			double amax = getAmax(i, i + 1, vprev);
			double s1 = 1.0 * (i - 1) / points;
			double s2 = 1.0 * i / points;
			double deltaT = 1.0 * (path.getV(s1) * (s2 - s1)) / velocities[i - 1];
			// System.out.println(deltaT);
			if (vprev + amax * deltaT < vmax) {
				velocities[i] = vprev + amax * deltaT;
			} else {
				velocities[i] = vmax;
			}
			// velocities[i] = Math.min(vmax, vprev + amax * deltaT);
		}
	}

	/**
	 * Computes the trajectory in reverse (call this AFTER
	 * computeForwardTrajectory) Overwrites any infeasible velocity values that
	 * were computed by the forward trajectory
	 */
	private void computeReverseTrajectory() {
		for (int i = points - 2; i > 0; i--) {
			double vprev = velocities[i + 1];
			double vmax = getVmax(i);
			double amax = getAmax(i, i + 1, vprev);
			double s1 = 1.0 * (i + 1) / points;
			double s2 = 1.0 * i / points;
			double deltaT = (path.getV(s1) * (s1 - s2)) / velocities[i + 1];
			if (vprev + amax * deltaT < vmax) {
				velocities[i] = Math.min(velocities[i], vprev + amax * deltaT);
			} else {
				velocities[i] = Math.min(velocities[i], vmax);
			}

		}
	}

	/**
	 * Gets the maximum velocity allowed at a given point
	 * 
	 * @param i
	 *            - the point being evaluated
	 * @return the maximum allowed velocity
	 */
	private double getVmax(int i) {
		// point on the spline function
		double s = 1.0 * i / points;
		// dtheta/dL (not with respect to t)
		double w = Math.abs(path.getW(s));
		// d^2theta/dL^2 (not with respect to t)
		double alpha = Math.abs(path.getAlpha(s)) + .000001; // Prevent division
																// by zero
		// velocity limit due to tradeoff between linear and angular velocity
		double v1 = vmax / (1.0 + w * vmax / wmax);
		// velocity limit due to maximum angular acceleration
		double v2 = Math.sqrt(alphamax / alpha);
		// return the smallest velocity limit
		return Math.min(v1, v2);
	}

	/**
	 * Gets the maximum acceleration allowed at a given point and velocity
	 * 
	 * @param i1
	 *            - the point being evaluated
	 * @param i2
	 *            - the next point on the path
	 * @param v
	 *            - the current velocity
	 * @return the maximum allowed acceleration (accurate to 3 decimal places)
	 */
	private double getAmax(int i1, int i2, double v) {
		// old point on the spline function
		double s0 = 1.0 * (i1) / velocities.length;
		// new point on the spline function
		double s1 = 1.0 * (i2) / velocities.length;
		// change in arc length between the two points
		// double l = Math.abs(path.getL(s1) - path.getL(s0));
		double l = path.getV(s0) * (s1 - s0);
		// initial dtheta/dL
		double w0 = Math.abs(path.getW(s0));
		// final dtheta/dL
		double w1 = Math.abs(path.getW(s1));
		// acceleration limit due to tradeoff between linear and angular
		// acceleration
		double a = amax / 2;

		for (int j = 2; j <= Math.log(1000) / Math.log(2) + 1; j++) {
			double sqrt = Math.sqrt(v * v + 2 * a * l); // final velocity
			double alpha = (w1 * a * sqrt - w0 * a * v) / (-v + sqrt); // curvature
																		// *
																		// acceleration
			if (a < amax - amax * Math.abs(alpha) / alphamax) { // Linear
																// Tradeoff
				a += amax / Math.pow(2, j);
			} else {
				a -= amax / Math.pow(2, j);
			}
		}
		return a;
	}

	/**
	 * Gets the target velocity for a given point
	 * 
	 * @param i
	 *            - the index of the point
	 * @return the target velocity
	 */
	public double getV(int i) {
		return velocities[i];
	}

	/**
	 * Gets the target angular velocity for a given point
	 * 
	 * @param i
	 *            - the index of the point
	 * @return the target angular velocity
	 */
	public double getW(int i) {
		double s = 1.0 * i / velocities.length;
		return path.getW(s) * velocities[i];
	}

	/**
	 * Determines the index of a point on the path
	 * 
	 * @param distance
	 *            - the distance traveled along the path
	 * @return the index of the current point
	 */
	public int getCurrentIndex(double distance) {
		return (int) (path.getS(distance) * velocities.length);
	}

	public int getDisplacement(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
}