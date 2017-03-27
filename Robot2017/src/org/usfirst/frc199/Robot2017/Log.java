package org.usfirst.frc199.Robot2017;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Log {

	public Log() {
		
	}
	
	public enum Level { 
		ERROR, WARN, INFO, DEBUG
	}
	
	public static Level level = getLogLevelFromDashboard();
	
	public static void log(Level l, String message) {
		if(l.compareTo(Log.level) <= 0) {
			System.out.println(message);
		}
	}
	
	public static void debug(String message) {
		log(Level.DEBUG, message);
	}
	
	public static Level getLogLevelFromDashboard() {
		String levelString = SmartDashboard.getString("Log Level At Startup","ERROR");
		if(levelString.toUpperCase().equals("ERROR")) {
			return Level.ERROR;
		} else if(levelString.toUpperCase().equals("WARN")) {
			return Level.WARN;
		} else if(levelString.toUpperCase().equals("INFO")) {
			return Level.INFO;
		} else if (levelString.toUpperCase().equals("DEBUG")){
			return Level.DEBUG;
		} else {
			System.out.println("Unrecognized log level: " + levelString + ". Please input Error, Warn, Info, or Debug.");
			return Level.ERROR;
		}
	}

}
