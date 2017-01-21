package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.RunFeeder;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class RunFeederTest {
	
	@Test
	public void test(){
		ShooterInterface mockShooter = mock(ShooterInterface.class);
		
		RunFeeder testCommand = new RunFeeder(.2, mockShooter);
		
		testCommand.execute();
		verify(mockShooter).runFeederMotor(.2);
		
		testCommand.end();
		verify(mockShooter).runFeederMotor(0);
	}
}
