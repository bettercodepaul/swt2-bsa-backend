package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import org.junit.Test;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getMigrationTimestampBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MigrationTimestampBETest {

	private static final Timestamp SYNCTIMESTAMP = null;
	private static final Long ID = 1L;


	@Test
	public void testToString() {
		final MigrationTimestampBE migrationTimestampBE = getMigrationTimestampBE();
		migrationTimestampBE.setSyncTimestamp(SYNCTIMESTAMP);
		migrationTimestampBE.setId(ID);

		final String actual = migrationTimestampBE.toString();

		assertThat(actual)
				.isNotEmpty()
				.contains(Long.toString(ID))
				.contains("null");

	}

	@Test
	public void testGetId(){
		MigrationTimestampBE actual = getMigrationTimestampBE();
		Long actualId = actual.getId();

		assertEquals(ID, actualId);
	}

	@Test
	public void testGetTimestamp(){
		MigrationTimestampBE actual = getMigrationTimestampBE();
		Timestamp actualTimestamp = actual.getSyncTimestamp();

		assertEquals(SYNCTIMESTAMP, actualTimestamp);
	}
}
