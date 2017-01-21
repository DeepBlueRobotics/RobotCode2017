package tests;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.RunIntake;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;

public class RunIntakeTest {
	
	@Test
	public void test(){
		IntakeInterface mockIntake = mock(IntakeInterface.class);
		
		RunIntake testCommand = new RunIntake(0, mockIntake);
		
		testCommand.execute();
		verify(mockIntake).runIntake(0);
		
		testCommand.end();
		verify(mockIntake).stopIntake();
	}
}
