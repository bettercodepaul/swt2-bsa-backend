package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Test;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChangeType enum
 *
 */
public class MigrationChangeTypeTest {

	private MigrationChangeType migrationChangeType;

	@Test
	public void testType(){
		assertThat(migrationChangeType.CREATE, notNullValue());
		assertThat(migrationChangeType.UPDATE, notNullValue());

		assertEquals("CREATE", migrationChangeType.CREATE.name());
		assertEquals("UPDATE", migrationChangeType.UPDATE.name());
	}
}
