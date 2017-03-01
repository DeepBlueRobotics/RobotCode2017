package tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.AutoDrive;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;

public class AutoDriveTestThatWorks {

	@Test
	public void test() {
		DrivetrainInterface drivetrain = mock(DrivetrainInterface.class);
		AutoDrive autoDrive = new AutoDrive(0, 0, drivetrain);

		autoDrive.initialize();
		verify(drivetrain).resetEncoder();
		verify(drivetrain).resetGyro();
		verify(drivetrain).setDistanceTarget(0);
		verify(drivetrain).setAngleTarget(0);

		autoDrive.execute();
		verify(drivetrain).autoDrive();
		verify(drivetrain).currentControl();

		autoDrive.isFinished();
		verify(drivetrain).distanceReachedTarget();

		autoDrive.end();
		verify(drivetrain).stopDrive();
	}

}
