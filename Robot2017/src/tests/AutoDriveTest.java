package tests;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.AutoDrive;
import org.usfirst.frc199.Robot2017.motion.PID;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

import edu.wpi.first.wpilibj.PIDInterface;

public class AutoDriveTest {
	
	@Test
	public void test(){
		DrivetrainInterface mockDrivetrain = mock(DrivetrainInterface.class);
		PIDInterface mockPID = mock(PIDInterface.class);
		PIDInterface mockPID2 = mock(PIDInterface.class);
		
		AutoDrive testCommand = new AutoDrive(0, 1, mockDrivetrain);
		when(mockDrivetrain.getDistancePID()).thenReturn((PID)mockPID);
		when(mockDrivetrain.getAnglePID()).thenReturn((PID)mockPID2);
		
		testCommand.initialize();
		verify(mockDrivetrain).resetEncoder();
		verify(mockDrivetrain).resetGyro();
		verify(mockDrivetrain).getDistancePID();
		verify(mockDrivetrain).getAnglePID();
		((PID) verify(mockPID)).setTarget(0);
		((PID) verify(mockPID2)).setTarget(1);
		
		testCommand.execute();
		verify(mockDrivetrain).autoDrive();
		verify(mockDrivetrain).currentControl();
		verify(mockDrivetrain).shiftGears();
		
		testCommand.isFinished();
		verify(mockDrivetrain).getDistancePID();
		verify(mockDrivetrain).getAnglePID();
		((PID) verify(mockPID)).reachedTarget();
		((PID) verify(mockPID2)).reachedTarget();
		
		testCommand.end();
		verify(mockDrivetrain).stopDrive();
	}
}
