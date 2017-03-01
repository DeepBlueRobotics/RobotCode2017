package tests;

import org.junit.Test;
import static org.mockito.Mockito.*;

import org.usfirst.frc199.Robot2017.commands.GradualDrive;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

public class GradualDriveTest {

	@Test
	public void test() {
		DrivetrainInterface mockDrivetrain = mock(DrivetrainInterface.class);

		GradualDrive testCommand = new GradualDrive(mockDrivetrain);

		testCommand.execute();
		verify(mockDrivetrain).gradualDrive();
		verify(mockDrivetrain).currentControl();
	}
}
