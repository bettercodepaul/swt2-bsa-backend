package de.bogenliga.application.services.v1.trigger.model;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the MigrationChange class
 *
 */
public class TriggerChangeTest<T extends AltsystemDO> {

	private static final long triggeringUserId = 21;

	private static final Long ID = 4L;
	private static final String KATEGORIE = "re";
	private static final Long ALTSYSTEM_ID = 5L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "ree";
	private static final OffsetDateTime CREATED_AT_UTC = OffsetDateTime.MIN;
	private static final OffsetDateTime RUN_AT_UTC = OffsetDateTime.MIN;


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	public AltsystemEntity<T> altsystemEntity;
	@Mock
	public TriggerDO triggerDO;
	@Mock
	public T altsystemDataObject;



	private TriggerDO getExpectedDO(){
		return new TriggerDO(ID, KATEGORIE, ALTSYSTEM_ID, OPERATION, STATUS, NACHRICHT, CREATED_AT_UTC, RUN_AT_UTC);
	}

	private TriggerChange getExpectedTC(){
		return new TriggerChange(triggerDO, altsystemDataObject, altsystemEntity, triggeringUserId);
	}

	@Test
	public void testTryMigration() {

		TriggerDO triggerData = getExpectedDO();

		TriggerChange<T> triggerChange = new TriggerChange<>(triggerData, altsystemDataObject, altsystemEntity, triggeringUserId);

		boolean result = triggerChange.tryMigration();

		assertFalse(result);
		assertEquals(TriggerChangeStatus.ERROR, triggerChange.getState());
	}


	@Test
	public void testGetAltsystemDataObject(){
		TriggerChange actual = getExpectedTC();
		AltsystemDO actualAltsystemDataObject = actual.getAltsystemDataObject();

		assertEquals(altsystemDataObject, actualAltsystemDataObject);
	}

}




