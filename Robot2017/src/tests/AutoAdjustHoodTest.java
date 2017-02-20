package tests;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.commands.AutoAdjustHood;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class AutoAdjustHoodTest {
	
	@Test
	public void test(){
		ShooterInterface mockShooter = mock(ShooterInterface.class);
		
		AutoAdjustHood testCommand = new AutoAdjustHood(mockShooter);
		
		testCommand.initialize();
		verify(mockShooter).setHoodPIDTarget(0);
		
		testCommand.execute();
		verify(mockShooter).updateHoodPID(0);
		verify(mockShooter).getHoodEncoder();
		verify(mockShooter).getHoodPIDOutput();
		verify(mockShooter).runHoodMotor(0);
		
		testCommand.isFinished();
		verify(mockShooter).hoodPIDTargetReached();
		
		testCommand.end();
		verify(mockShooter).stopHoodMotor();
	}
}
