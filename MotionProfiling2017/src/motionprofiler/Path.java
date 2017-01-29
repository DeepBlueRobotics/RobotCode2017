package motionprofiler;

/**
 * A path defined by parametric cubic functions of a parameter s.
 * s is a spline that represents progress along the path as a value from 0 to 1.
 */
public class Path {
	
	// Constants that define the path
	private final double Ax, Bx, Cx, Dx, Ay, By, Cy, Dy;
	// Used to boost efficiency in taking a numerical integral multiple times
	private double lastL = 0, lastS = 0;
	
	/**
	 * Generates a path that satisfies the given constraints
	 * @param x0 - initial x
	 * @param y0 - initial y
	 * @param x1 - final x
	 * @param y1 - final y
	 * @param angle0 - initial angle (degrees clockwise from <0, 1>)
	 * @param angle1 - final angle
	 * @param k - Determines how sharply the curve turns (2 usually works well)
	 */
	public Path(double x0, double y0, double x1, double y1, double angle0, double angle1, double k) {
		double dx = x1-x0;
		double dy = y1-y0;
		double vx0 = Math.sin(Math.toRadians(angle0))*k*Math.sqrt(dx*dx+dy*dy);
		double vy0 = Math.cos(Math.toRadians(angle0))*k*Math.sqrt(dx*dx+dy*dy); // Sam was here.
		double vx1 = Math.sin(Math.toRadians(angle1))*k*Math.sqrt(dx*dx+dy*dy);
		double vy1 = Math.cos(Math.toRadians(angle1))*k*Math.sqrt(dx*dx+dy*dy);
		Ax = vx1+vx0-2*dx;
		Bx = -vx1-2*vx0+3*dx;
		Cx = vx0;
		Dx = x0;
		Ay = vy1+vy0-2*dy;
		By = -vy1-2*vy0+3*dy;
		Cy = vy0;
		Dy = y0;
	}

	/**
	 * Gets x value at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return x(s)
	 */
	public double getX(double s) {
		return Ax*s*s*s+Bx*s*s+Cx*s+Dx;
	}

	/**
	 * Gets y value at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return y(s)
	 */
	public double getY(double s) {
		return Ay*s*s*s+By*s*s+Cy*s+Dy;
	}
	
	/**
	 * Gets x velocity value at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return dx/ds(s)
	 */
	public double getVx(double s) {
		return 3*Ax*s*s+2*Bx*s+Cx;
	}

	/**
	 * Gets y velocity value at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return dy/ds(s)
	 */
	public double getVy(double s) {
		return 3*Ay*s*s+2*By*s+Cy;
	}
	
	/**
	 * Gets y velocity value at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return dy/ds(s)
	 */
	public double getV(double s) {
		return Math.sqrt(Math.pow(getVx(s), 2)+Math.pow(getVy(s), 2));
	}
	
	/**
	 * Gets the direction of travel at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return angle (degrees clockwise from <0, 1>)
	 */
	public double getTheta(double s) {
		return 90-Math.toDegrees(Math.atan2(getVy(s), getVx(s)));
	}
	
	/**
	 * Gets angular velocity with respect to arc length at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return dtheta/dL(s)
	 */
	public double getW(double s) {
		double ds = .000001;
		double dl = ds*getV(s);
		double theta0 = getTheta(s);
		double theta1 = getTheta(s+ds);
		return (theta1-theta0)/dl;
	}
	
	/**
	 * Gets angular acceleration with respect to arc length at a point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return d^2theta/dL^2(s)
	 */
	public double getAlpha(double s) {
		double ds = .000001;
		double dl = ds*getV(s);
		double w0 = getW(s);
		double w1 = getW(s+ds);
		return (w1-w0)/dl;
	}

	/**
	 * Gets arc length from zero to a given point on the path
	 * @param s - the point on the path from 0 to 1
	 * @return length of path from 0 to s
	 */
	public double getL(double s) {
		double l = 0;
		double ds = 0.0001;
		for(double i = 0; i<s; i+=ds) {
			l+=getV(s)*ds;
		}
		return l;
	}

	/**
	 * Gets the s value for a given arc length
	 * Stores previous value to improve efficiency
	 * @param L - arc length
	 * @return the point on the spline
	 */
	public double getS(double L) {
		if(L<lastL) {
			lastL = 0;
			lastS = 0;
		}
		double l = lastL, ds = .00001;
		for(double s=lastS; s<1.0; s+=ds) {
			if(l >= L) {
				lastS = s;
				lastL = l;
				return s;
			}
			l+=getV(s)*ds;
		}
		return 1.0;
	}
}
