package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.ToggleDrivetrainShift;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

public class ToggleDrivetrainShiftTest {
	@Test
	public void test() {
		DrivetrainInterface mockDriver = mock(DrivetrainInterface.class);

		ToggleDrivetrainShift testCommand = new ToggleDrivetrainShift(mockDriver);

		testCommand.execute();
		verify(mockDriver).shiftGears();

	}
}
