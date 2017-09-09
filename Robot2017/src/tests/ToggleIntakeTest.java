package tests;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.ToggleGearIntake;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

public class ToggleIntakeTest {
	// doesn't work
	@Test
	public void test() {
		IntakeInterface mockIntake = mock(IntakeInterface.class);

		ToggleGearIntake testCommand = new ToggleGearIntake(false, false, mockIntake);

		testCommand.execute();
		verify(mockIntake).toggleIntake(false, false);

		testCommand.end();
		verify(mockIntake).setIntakePistonNeutral();
	}
}
