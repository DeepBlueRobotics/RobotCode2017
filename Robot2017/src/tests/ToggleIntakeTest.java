package tests;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.ToggleIntake;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

public class ToggleIntakeTest {
	@Test
	public void test() {
		IntakeInterface mockIntake = mock(IntakeInterface.class);

		ToggleIntake testCommand = new ToggleIntake(mockIntake);

		testCommand.execute();
		verify(mockIntake).toggleIntake();

	}
}
