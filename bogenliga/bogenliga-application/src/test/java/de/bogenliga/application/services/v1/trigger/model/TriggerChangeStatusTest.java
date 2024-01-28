package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChangeState enum
 *
 */
public class TriggerChangeStatusTest {

	private TriggerChangeStatus triggerChangeStatus;

	@Test
	public void testState() {
		assertThat(triggerChangeStatus.NEW, notNullValue());
		assertThat(triggerChangeStatus.IN_PROGRESS, notNullValue());
		assertThat(triggerChangeStatus.ERROR, notNullValue());
		assertThat(triggerChangeStatus.SUCCESS, notNullValue());

		assertEquals("NEW", triggerChangeStatus.NEW.name());
		assertEquals("IN_PROGRESS", triggerChangeStatus.IN_PROGRESS.name());
		assertEquals("ERROR", triggerChangeStatus.ERROR.name());
		assertEquals("SUCCESS", triggerChangeStatus.SUCCESS.name());
	}
}
