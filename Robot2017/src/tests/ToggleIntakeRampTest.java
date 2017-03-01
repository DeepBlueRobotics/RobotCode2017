package tests;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.ToggleIntakeRamp;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

public class ToggleIntakeRampTest {

	@Test
	public void test() {
		IntakeInterface intake = mock(IntakeInterface.class);
		ToggleIntakeRamp myCommand = new ToggleIntakeRamp(intake);

		myCommand.execute();
		verify(intake).toggleFlipperFlapper();

		myCommand.end();
		verify(intake).setFlipperFlapperNeutral();
	}

}
