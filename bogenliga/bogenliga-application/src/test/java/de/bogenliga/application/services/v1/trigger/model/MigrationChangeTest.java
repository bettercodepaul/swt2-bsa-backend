package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import static org.junit.Assert.*;

/**
 * Tests the MigrationChange class
 *
 */
public class MigrationChangeTest<T extends AltsystemDO> {


	private static final MigrationChangeState isNew = MigrationChangeState.NEW;
	private static final MigrationChangeState isInProgress = MigrationChangeState.IN_PROGRESS;
	private static final MigrationChangeState isError= MigrationChangeState.ERROR;
	private static final MigrationChangeState isSuccess = MigrationChangeState.SUCCESS;
	private static final MigrationChangeType isUpdate = MigrationChangeType.UPDATE;
	private static final MigrationChangeType isCreate = MigrationChangeType.CREATE;


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	public AltsystemDO altsystemDO;

	@Mock
	public AltsystemEntity altsystemEntity;

	@InjectMocks
	public MigrationChange migrationChange;


	//NEW, IN_PROGRESS, ERROR, SUCCESS
	@Test
	public void testTryMigration(){
		//TODO
	}


	@Test
	public void testGetSetState(){

		MigrationChangeState actual = migrationChange.getState();
		assertNull(actual);

		migrationChange.setState(MigrationChangeState.NEW);
		actual = migrationChange.getState();
		assertEquals(isNew, actual);

		migrationChange.setState(MigrationChangeState.IN_PROGRESS);
		actual = migrationChange.getState();
		assertEquals(isInProgress, actual);

		migrationChange.setState(MigrationChangeState.ERROR);
		actual = migrationChange.getState();
		assertEquals(isError, actual);

		migrationChange.setState(MigrationChangeState.SUCCESS);
		actual = migrationChange.getState();
		assertEquals(isSuccess, actual);
	}


}
