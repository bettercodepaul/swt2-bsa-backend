package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Rule;
import org.junit.Test;
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

/**
 * Tests the MigrationChange class
 *
 */
public class TriggerChangeTest<T extends AltsystemDO> {


	private static final TriggerChangeStatus New = TriggerChangeStatus.NEW;
	private static final TriggerChangeStatus InProgress = TriggerChangeStatus.IN_PROGRESS;
	private static final TriggerChangeStatus Error= TriggerChangeStatus.ERROR;
	private static final TriggerChangeStatus Success = TriggerChangeStatus.SUCCESS;
	private static final long triggeringUserId = 21;


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	public AltsystemDO altsystemDO;
	@Mock
	public AltsystemEntity<T> altsystemEntity;
	@Mock
	public TriggerDO triggerDO;
	@Mock
	public T altsystemDataObject;



	private TriggerChange getExpectedTC(){
		return new TriggerChange(triggerDO, altsystemDataObject, altsystemEntity, triggeringUserId);
	}

	@Test
	public void testGetId(){
		TriggerChange actual = getExpectedTC();
		AltsystemDO actualAltsystemDataObject = actual.getAltsystemDataObject();

		assertEquals(altsystemDataObject, actualAltsystemDataObject);
	}



	//NEW, IN_PROGRESS, ERROR, SUCCESS
	//@Test
	public void testTryMigration(){
		//TODO
	}



}
