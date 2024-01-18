package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChange class
 *
 */
public class TriggerChangeTest<T extends AltsystemDO> {


	private static final TriggerChangeStatus isNew = TriggerChangeStatus.NEW;
	private static final TriggerChangeStatus isInProgress = TriggerChangeStatus.IN_PROGRESS;
	private static final TriggerChangeStatus isError= TriggerChangeStatus.ERROR;
	private static final TriggerChangeStatus isSuccess = TriggerChangeStatus.SUCCESS;
	private static final TriggerChangeOperation isUpdate = TriggerChangeOperation.UPDATE;
	private static final TriggerChangeOperation isCreate = TriggerChangeOperation.CREATE;


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	public AltsystemDO altsystemDO;

	@Mock
	public AltsystemEntity altsystemEntity;

	@InjectMocks
	public TriggerChange triggerChange;


	//NEW, IN_PROGRESS, ERROR, SUCCESS
	//@Test
	public void testTryMigration(){
		//TODO
	}


	//@Test
	public void testGetSetState(){

		TriggerChangeStatus actual = triggerChange.getState();
		assertNull(actual);

		triggerChange.setState(TriggerChangeStatus.NEW);
		actual = triggerChange.getState();
		assertEquals(isNew, actual);

		triggerChange.setState(TriggerChangeStatus.IN_PROGRESS);
		actual = triggerChange.getState();
		assertEquals(isInProgress, actual);

		triggerChange.setState(TriggerChangeStatus.ERROR);
		actual = triggerChange.getState();
		assertEquals(isError, actual);

		triggerChange.setState(TriggerChangeStatus.SUCCESS);
		actual = triggerChange.getState();
		assertEquals(isSuccess, actual);
	}


}
