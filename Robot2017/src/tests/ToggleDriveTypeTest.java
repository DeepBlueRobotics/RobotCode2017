package tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.ToggleDriveType;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

public class ToggleDriveTypeTest {

	@Test
	public void test() {
		DrivetrainInterface drive = mock(DrivetrainInterface.class);
		ToggleDriveType myCommand = new ToggleDriveType(drive);
		
		myCommand.execute();
		verify(drive).toggleDriveType();
	}

}
