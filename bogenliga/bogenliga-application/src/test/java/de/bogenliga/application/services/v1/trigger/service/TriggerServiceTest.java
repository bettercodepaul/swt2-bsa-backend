package de.bogenliga.application.services.v1.trigger.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerServiceTest {

	private LocalDateTime currentTime = LocalDateTime.now();

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private TriggerService triggerService;

	@InjectMocks
	private TriggerService triggerTest;





	@Test
	public void getPong(){
		// Actual result(s) TBD \\
		assertThat(triggerTest.ping()).isEqualTo("pong");

	}


	@Test
	public void validateSyncTimestamp() throws NoSuchFieldException, IllegalAccessException{

		Field syncTimestampField = triggerTest.getClass().getDeclaredField("syncTimestamp");
		assertEquals(LocalDateTime.class, syncTimestampField.getType());

		syncTimestampField.setAccessible(true); // Enables Access
		assertNull(syncTimestampField.get(triggerTest)); // Tests if syncTimestamp has it's default value of null
		syncTimestampField.set(triggerTest, currentTime); // Sets syncTimestamp to current time
		assertNotNull(syncTimestampField.get(triggerTest)); // Tests if syncTimestamp has been set
	}


	// To be changed
	@Test
	public void testTrigger(){
		TriggerService triggerServiceSpy = Mockito.spy(new TriggerService()); // Create spy
		doNothing().when(triggerServiceSpy).triggerSchedule(); // Call void method

		triggerServiceSpy.triggerSchedule();

		verify(triggerServiceSpy, times(1)).triggerSchedule(); // Test if invoked once
	}
}
