package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeState;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChangeState enum
 *
 */
public class MigrationChangeStateTest {

	private MigrationChangeState migrationChangeState;

	@Test
	public void testState() {
		assertThat(migrationChangeState.NEW, notNullValue());
		assertThat(migrationChangeState.IN_PROGRESS, notNullValue());
		assertThat(migrationChangeState.ERROR, notNullValue());
		assertThat(migrationChangeState.SUCCESS, notNullValue());

		assertEquals("NEW", migrationChangeState.NEW.name());
		assertEquals("IN_PROGRESS", migrationChangeState.IN_PROGRESS.name());
		assertEquals("ERROR", migrationChangeState.ERROR.name());
		assertEquals("SUCCESS", migrationChangeState.SUCCESS.name());
	}
}
