package org.usfirst.frc199.Robot2017.commands;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.usfirst.frc199.Robot2017.Robot;
import org.usfirst.frc199.Robot2017.subsystems.ClimberInterface;
import org.usfirst.frc199.Robot2017.subsystems.IntakeInterface;
import org.usfirst.frc199.Robot2017.subsystems.ShooterInterface;

public class ManualControlMechsTest {

	@Test
	public void test() {
		IntakeInterface mockIntake = mock(IntakeInterface.class);
		ShooterInterface mockShooter = mock(ShooterInterface.class);
		ClimberInterface mockClimber = mock(ClimberInterface.class);
		
		ManualControlMechs testCommand = new ManualControlMechs(mockIntake, mockShooter, mockClimber);
		
		testCommand.manualSwitch("SHOOTER", 0);
		verify(mockShooter).runShootMotor(0);
		
		testCommand.manualSwitch("intake", -.444);
		verify(mockIntake).runIntake(-.444);
		
		testCommand.manualSwitch(":DDD", 1);
		verifyNoMoreInteractions(mockIntake);
		verifyNoMoreInteractions(mockShooter);
		verifyNoMoreInteractions(mockClimber);
		
	}

}
