package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.RunShooter;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class RunShooterTest {

	@Test
	public void test() {
		ShooterInterface mockShooter = mock(ShooterInterface.class);

		RunShooter testCommand = new RunShooter(0.2, mockShooter, 0);

		testCommand.execute();
		verify(mockShooter).shooterMotorStalled();

		testCommand.end();
		verify(mockShooter).runShootMotor(0);

	}
}
