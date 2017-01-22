package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.commands.TeleopDrive;
import org.usfirst.frc199.Robot2017.subsystems.Drivetrain;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

public class TeleopDriveTest {
	
	@Test
	public void test() {
		DrivetrainInterface testTeleopDrive = mock(DrivetrainInterface.class);

		Robot myRobit = new Robot();
		myRobit.robotInit();
		TeleopDrive testDrive = new TeleopDrive(testTeleopDrive);
		testDrive.execute();
		
		verify(testTeleopDrive).drive();
		
		verify(testTeleopDrive).currentControl();
		verify(testTeleopDrive).shiftGears();
	}
}
