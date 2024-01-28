package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChangeType enum
 *
 */
public class TriggerChangeOperationTest {

	private TriggerChangeOperation triggerChangeOperation;

	@Test
	public void testType(){
		assertThat(triggerChangeOperation.CREATE, notNullValue());
		assertThat(triggerChangeOperation.UPDATE, notNullValue());

		assertEquals("CREATE", triggerChangeOperation.CREATE.name());
		assertEquals("UPDATE", triggerChangeOperation.UPDATE.name());
	}
}
