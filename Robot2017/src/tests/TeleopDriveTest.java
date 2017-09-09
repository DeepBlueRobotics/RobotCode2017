package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.commands.TeleopDrive;
import org.usfirst.frc199.Robot2017.subsystems.Drivetrain;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class TeleopDriveTest {

	@Test
	public void test() {
		DrivetrainInterface testTeleopDrive = mock(DrivetrainInterface.class);
		ShooterInterface testManipulatorTurret = mock(ShooterInterface.class);

		TeleopDrive testDrive = new TeleopDrive(testTeleopDrive, testManipulatorTurret);
		testDrive.execute();

		verify(testTeleopDrive).drive();

		verify(testTeleopDrive).currentControl();
		// verify(testTeleopDrive).shiftGears(); This doesn't get called when
		// line 22 returns false
	}
}
