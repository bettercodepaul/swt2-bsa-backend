package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import org.junit.Test;
import org.springframework.scheduling.Trigger;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getMigrationTimestampBE;
import static org.assertj.core.api.Assertions.assertThat;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;
import static org.junit.Assert.*;

/**
 * Tests the TriggerBE class
 *
 * @author Lino Cortese, insomnia solutions bed & sleep gmbh
 */
public class TriggerBETest {

	private static final Long ID = 123L;
	private static final String KATEGORIE = "sleep";
	private static final Long ALTSYSTEM_ID = 234L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "invalid";
	private static final Timestamp CREATED_AT_UTC = null;
	private static final Timestamp RUN_AT_UTC = null;
	private static final Long CHANGE_OPERATION_ID = null;
	private static final Long CHANGE_STATUS_ID = null;

	//Test data for setters
	private static final Long newCHANGE_OPERATION_ID = 123L;
	private static final Long newCHANGE_STATUS_ID = 123L;



	@Test
	public void testToString() {
		final TriggerBE triggerBE = getTriggerBE();
		triggerBE.setId(ID);
		triggerBE.setKategorie(KATEGORIE);
		triggerBE.setAltsystemId(ALTSYSTEM_ID);
		triggerBE.setChangeOperation(OPERATION);
		triggerBE.setChangeStatus(STATUS);
		triggerBE.setNachricht(NACHRICHT);
		triggerBE.setRunAtUtc(CREATED_AT_UTC);
		triggerBE.setRunAtUtc(RUN_AT_UTC);

		final String actual = triggerBE.toString();

		assertThat(actual)
				.isNotEmpty()
				.contains(Long.toString(ID))
				.contains(KATEGORIE);
	}
	@Test
	public void testGetChangeStatus(){
		TriggerBE actual = getTriggerBE();
		TriggerChangeStatus actualChangeStatus = actual.getChangeStatus();

		assertEquals(STATUS, actualChangeStatus);
	}
	@Test
	public void testGetChangeOperation(){
		TriggerBE actual = getTriggerBE();
		TriggerChangeOperation actualChangeOperation = actual.getChangeOperation();

		assertEquals(OPERATION, actualChangeOperation);
	}
	@Test
	public void testGetChangeOperationId(){
		RawTriggerBE actual = getTriggerBE();
		Long actualChangeOperationID = actual.getChangeOperationId();

		assertEquals(CHANGE_OPERATION_ID, actualChangeOperationID);
	}
	@Test
	public void testSetChangeOperationId(){
		TriggerBE actual = getTriggerBE();
		actual.setChangeOperationId(newCHANGE_OPERATION_ID);
		Long actualChangeOperationId = actual.getChangeOperationId();

		assertEquals(newCHANGE_OPERATION_ID, actualChangeOperationId);
	}
	@Test
	public void testGetChangeStatusID(){
		RawTriggerBE actual = getTriggerBE();
		Long actualChangeStatusID = actual.getChangeStatusId();

		assertEquals(CHANGE_STATUS_ID, actualChangeStatusID);
	}
	@Test
	public void testSetChangeStatusId(){
		TriggerBE actual = getTriggerBE();
		actual.setChangeStatusId(newCHANGE_STATUS_ID);
		Long actualChangeStatusId = actual.getChangeStatusId();

		assertEquals(newCHANGE_STATUS_ID, actualChangeStatusId);
	}

}
