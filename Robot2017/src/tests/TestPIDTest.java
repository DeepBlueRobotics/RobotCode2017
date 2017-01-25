package tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.TestPID;
import org.usfirst.frc199.Robot2017.subsystems.DrivetrainInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class TestPIDTest {

	@Test
	public void test() {
		DrivetrainInterface mockDrive = mock(DrivetrainInterface.class);
		ShooterInterface mockShoot = mock(ShooterInterface.class);

		TestPID testShooter = new TestPID(TestPID.System.SHOOTER, mockShoot, mockDrive);
		testShooter.initialize();
		
		verify(mockShoot).setShooterPIDTarget(0);
		verify(mockShoot).updateShooterPID(mockShoot.currentSpeed());
		
		testShooter.execute();
		
		verify(mockShoot).runShootMotor(mockShoot.updateSpeed(0));
		
		testShooter.end();
		
		verify(mockShoot).runShootMotor(0.0);
		
		TestPID testDriveDist = new TestPID(TestPID.System.DRIVEDISTANCE, mockShoot, mockDrive);
		testDriveDist.initialize();
		
		verify(mockDrive).setDistanceTarget(0);
		verify(mockDrive).setAngleTarget(0);
		
		testDriveDist.execute();
		
		verify(mockDrive).autoDrive();
		
		testDriveDist.isFinished();
		
		verify(mockDrive).distanceReachedTarget();
		verify(mockDrive).angleReachedTarget();
		
		testDriveDist.end();
		
		verify(mockDrive).stopDrive();
		
		TestPID testDriveAng = new TestPID(TestPID.System.DRIVEANGLE, mockShoot, mockDrive);
		testDriveAng.initialize();
		
		verify(mockDrive).setAngleTarget(0);
		
		testDriveAng.execute();
		verify(mockDrive).updateAngle();
		
		testDriveAng.isFinished();
		verify(mockDrive).angleReachedTarget();
		
		TestPID testDriveVel = new TestPID(TestPID.System.DRIVEVELOCITY, mockShoot, mockDrive);
		testDriveVel.initialize();
		
		verify(mockDrive).setVelocityTarget(0, 0);
		
		testDriveVel.execute();
		
		verify(mockDrive).updateVelocity();
		
		TestPID testDriveAngVel = new TestPID(TestPID.System.DRIVEANGULARVELOCITY, mockShoot, mockDrive);
		testDriveAngVel.initialize();
		
		verify(mockDrive).setVelocityTarget(0, 0);
		
		testDriveAngVel.execute();
		
		verify(mockDrive).updateVelocity();
		
	}

}
