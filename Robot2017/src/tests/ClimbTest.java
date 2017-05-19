package tests;

import org.junit.Test;
import static org.mockito.Mockito.*;

import org.usfirst.frc199.Robot2017.commands.Climb;
import org.usfirst.frc199.Robot2017.subsystems.ClimberInterface;

public class ClimbTest {

	@Test
	public void test() {
		ClimberInterface climber = mock(ClimberInterface.class);

		Climb climbCommand = new Climb(climber, 1);

		climbCommand.initialize();

		climbCommand.execute();

		climbCommand.end();
		verify(climber).stopWinch();

	}

}
