package de.bogenliga.application.services.v1.trigger.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bogenliga.application.business.altsystem.sync.OldDbImport;
import org.assertj.core.api.Java6Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.MigrationTimestampDAO;
import de.bogenliga.application.business.trigger.impl.entity.MigrationTimestampBE;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.services.v1.trigger.model.TriggerChange;
import de.bogenliga.application.services.v1.trigger.model.TriggerCountDTO;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;
import static org.mockito.Mockito.*;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerServiceTest {

	private static final Long TRIGGER_ID = 123L;
	private static final String TRIGGER_KATEGORIE = "bee";
	private static final Long TRIGGER_ALTSYSTEMID = 234L;
	private static final TriggerChangeOperation TRIGGER_OPERATION = null;
	private static final TriggerChangeStatus TRIGGER_STATUS = null;
	private static final TriggerChangeStatus TRIGGER_STATUS_ERROR = TriggerChangeStatus.ERROR;
	private static final TriggerChangeStatus TRIGGER_STATUS_NEW = TriggerChangeStatus.NEW;
	private static final TriggerChangeStatus TRIGGER_STATUS_SUCCSESS = TriggerChangeStatus.SUCCESS;
	private static final TriggerChangeStatus TRIGGER_STATUS_IN_PROGRESS = TriggerChangeStatus.IN_PROGRESS;
	private static final String TRIGGER_NACHRICHT = "see";
	public static final OffsetDateTime TRIGGER_CREATEDATUTCO = null;
	public static final OffsetDateTime TRIGGER_RUNATUTCO = null;
	public static final Long TRIGGERCOUNT_IN_PROGRESS_COUNT = 2L;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private BasicDAO basicDAO;

	@Mock
	private TriggerComponent triggerComponent;
	@Mock
	private MigrationTimestampDAO migrationTimestampDAO;
	@Mock
	private OldDbImport oldDBImport;

	@InjectMocks
	private TriggerService triggerServiceTest;


	public static TriggerCountDO getTriggerCountDO() {
		return new TriggerCountDO(
				TRIGGERCOUNT_IN_PROGRESS_COUNT
		);
	}
	public static TriggerDO getTriggerDO() {
		return new TriggerDO(
				TRIGGER_ID,
				TRIGGER_KATEGORIE,
				TRIGGER_ALTSYSTEMID,
				TRIGGER_OPERATION,
				TRIGGER_STATUS,
				TRIGGER_NACHRICHT,
				TRIGGER_CREATEDATUTCO,
				TRIGGER_RUNATUTCO
		);
	}
	public static TriggerDO getErrorTriggerDO() {
		return new TriggerDO(
				TRIGGER_ID,
				TRIGGER_KATEGORIE,
				TRIGGER_ALTSYSTEMID,
				TRIGGER_OPERATION,
				TRIGGER_STATUS_ERROR,
				TRIGGER_NACHRICHT,
				TRIGGER_CREATEDATUTCO,
				TRIGGER_RUNATUTCO
		);
	}
	public static TriggerDO getNewTriggerDO() {
		return new TriggerDO(
				TRIGGER_ID,
				TRIGGER_KATEGORIE,
				TRIGGER_ALTSYSTEMID,
				TRIGGER_OPERATION,
				TRIGGER_STATUS_NEW,
				TRIGGER_NACHRICHT,
				TRIGGER_CREATEDATUTCO,
				TRIGGER_RUNATUTCO
		);
	}
	public static TriggerDO getSuccsessTriggerDO() {
		return new TriggerDO(
				TRIGGER_ID,
				TRIGGER_KATEGORIE,
				TRIGGER_ALTSYSTEMID,
				TRIGGER_OPERATION,
				TRIGGER_STATUS_SUCCSESS,
				TRIGGER_NACHRICHT,
				TRIGGER_CREATEDATUTCO,
				TRIGGER_RUNATUTCO
		);
	}
	public static TriggerDO getInProgressTriggerDO() {
		return new TriggerDO(
				TRIGGER_ID,
				TRIGGER_KATEGORIE,
				TRIGGER_ALTSYSTEMID,
				TRIGGER_OPERATION,
				TRIGGER_STATUS_IN_PROGRESS,
				TRIGGER_NACHRICHT,
				TRIGGER_CREATEDATUTCO,
				TRIGGER_RUNATUTCO
		);
	}

	private static AltsystemDO createRawAltsystemDO(Timestamp createdAt, Timestamp updatedAt) {
		final AltsystemDO rawDO = new AltsystemLigaDO();
		rawDO.setCreatedAt(createdAt);
		rawDO.setUpdatedAt(updatedAt);
		return rawDO;
	}

	@Test
	public void testFindAll() {
		// prepare test data
		final TriggerDO expectedDO = getTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllLimited()).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAll();

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllLimited();
	}
	@Test
	public void testFindInProgressCount() {
		// prepare test data
		final TriggerCountDO expectedDO = getTriggerCountDO();

		// configure mocks
		when(triggerComponent.findInProgressCount()).thenReturn(expectedDO);

		// call test method
		final TriggerCountDTO actual = triggerServiceTest.findInProgressCount();

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull();

		// verify invocations
		verify(triggerComponent, times(1)).findInProgressCount();
	}
	@Test
	public void testFindUnprocessedCount() {
		// prepare test data
		final TriggerCountDO expectedDO = getTriggerCountDO();

		// configure mocks
		when(triggerComponent.findUnprocessedCount()).thenReturn(expectedDO);

		// call test method
		final TriggerCountDTO actual = triggerServiceTest.findUnprocessedCount();

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull();

		// verify invocations
		verify(triggerComponent, times(1)).findUnprocessedCount();
	}
	@Test
	public void testFindAllCount() {
		// prepare test data
		final TriggerCountDO expectedDO = getTriggerCountDO();

		// configure mocks
		when(triggerComponent.findAllCount()).thenReturn(expectedDO);

		// call test method
		final TriggerCountDTO actual = triggerServiceTest.findAllCount();

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull();

		// verify invocations
		verify(triggerComponent, times(1)).findAllCount();
	}
	@Test
	public void testFindAllErrors() {
		// prepare test data
		final TriggerDO expectedDO = getErrorTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllErrors("0","500", "1 MONTH")).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAllErrors("0","500", "1 MONTH");

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllErrors("0","500", "1 MONTH");
	}
	@Test
	public void testFindAllNews() {
		// prepare test data
		final TriggerDO expectedDO = getNewTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllNews("0","500", "1 MONTH")).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAllNews("0","500", "1 MONTH");

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllNews("0","500", "1 MONTH");
	}
	@Test
	public void testFindAllInProgress() {
		// prepare test data
		final TriggerDO expectedDO = getInProgressTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllInProgress("0","500", "1 MONTH")).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAllInProgress("0","500", "1 MONTH");

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllInProgress("0","500", "1 MONTH");
	}
	@Test
	public void testFindAllSuccess() {
		// prepare test data
		final TriggerDO expectedDO = getSuccsessTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllSuccessed("0","500", "1 MONTH")).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAllSuccessed("0","500", "1 MONTH");

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllSuccessed("0","500", "1 MONTH");
	}
	@Test
	public void testFindAllWithPages() {
		// prepare test data
		final TriggerDO expectedDO = getTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllWithPages("0","500", "1 MONTH")).thenReturn(expectedDOList);

		// call test method
		final List<TriggerDTO> actual = triggerServiceTest.findAllWithPages("0","500", "1 MONTH");

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();

		Java6Assertions.assertThat(actual.get(0).getId())
				.isEqualTo(expectedDO.getId());
		Java6Assertions.assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedDO.getKategorie());
		Java6Assertions.assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedDO.getAltsystemId());
		Java6Assertions.assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedDO.getNachricht());
		Java6Assertions.assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedDO.getCreatedAtUtc());
		Java6Assertions.assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedDO.getRunAtUtc());

		// verify invocations
		verify(triggerComponent, times(1)).findAllWithPages("0","500", "1 MONTH");
	}
	@Test
	public void testDeleteNewEntries() {
		// Call the method
		triggerComponent.deleteEntries("Neu", "1 MONTH");

		// Verify that triggerComponent.deleteEntries was called with the correct parameters
		verify(triggerComponent).deleteEntries("Neu", "1 MONTH");
	}
	@Test
	public void testDeleteErrorEntries() {
		// Call the method
		triggerComponent.deleteEntries("Fehlgeschlagen", "1 MONTH");

		// Verify that triggerComponent.deleteEntries was called with the correct parameters
		verify(triggerComponent).deleteEntries("Fehlgeschlagen", "1 MONTH");
	}
	@Test
	public void testDeleteInProgressEntries() {
		// Call the method
		triggerComponent.deleteEntries("Laufend", "1 MONTH");

		// Verify that triggerComponent.deleteEntries was called with the correct parameters
		verify(triggerComponent).deleteEntries("Laufend", "1 MONTH");
	}
	@Test
	public void testDeleteSuccessEntries() {
		// Call the method
		triggerComponent.deleteEntries("Erfolgreich", "1 MONTH");

		// Verify that triggerComponent.deleteEntries was called with the correct parameters
		verify(triggerComponent).deleteEntries("Erfolgreich", "1 MONTH");
	}
	@Test
	public void testDeleteAllEntries() {
		// Call the method
		triggerComponent.deleteEntries("Alle", "1 MONTH");

		// Verify that triggerComponent.deleteEntries was called with the correct parameters
		verify(triggerComponent).deleteEntries("Alle", "1 MONTH");
	}


	@Test
	public void testLoadUnprocessedChanges() {
		// prepare test data
		final TriggerDO expectedDO = getTriggerDO();
		expectedDO.setCreatedByUserId(1L); // A user ID is needed for TriggerChange wrapper
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAllUnprocessed()).thenReturn(expectedDOList);
		when(basicDAO.selectSingleEntity(any(), anyString(), any())).thenReturn(new AltsystemLigaDO());

		// call test method
		final List<TriggerChange<?>> actual = triggerServiceTest.loadUnprocessedChanges(1);

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();
		Java6Assertions.assertThat(actual.get(0).getData()).isEqualTo(expectedDO);

		// verify invocations
		verify(triggerComponent, times(1)).findAllUnprocessed();
	}

	@Test
	public void testComputeAllChanges() {
		// prepare test data
		Timestamp earlier = Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 0));
		Timestamp later = Timestamp.valueOf(LocalDateTime.of(2024, 1, 3, 0, 0));

		final TriggerDO unprocessedTrigger = getTriggerDO();
		unprocessedTrigger.setCreatedByUserId(1L);
		final List<TriggerDO> unprocessedTriggers = Collections.singletonList(unprocessedTrigger);

		final AltsystemDO unprocessedDO = createRawAltsystemDO(
				later,
				later
		);

		// configure mocks
		when(triggerComponent.findAllUnprocessed()).thenReturn(unprocessedTriggers);
		when(basicDAO.selectSingleEntity(any(), anyString(), any())).thenReturn(unprocessedDO);

		final AltsystemDO expectedDO = createRawAltsystemDO(
				later,
				later
		);
		final List<Object> expectedDOs = Collections.singletonList(expectedDO);
		final String sqlQuery = "SELECT * FROM altsystem_liga";
		when(basicDAO.selectEntityList(any(), eq(sqlQuery))).thenReturn(expectedDOs);

		// call test method
		final List<TriggerChange<?>> actual = triggerServiceTest.computeAllChanges(1L, earlier);

		// assert result
		Java6Assertions.assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);

		Java6Assertions.assertThat(actual.get(0)).isNotNull();
		Java6Assertions.assertThat(actual.get(1)).isNotNull();
		Java6Assertions.assertThat(actual.get(0).getData()).isEqualTo(unprocessedTrigger);
		Java6Assertions.assertThat(actual.get(1).getAltsystemDataObject()).isEqualTo(expectedDO);

		// verify invocations
		verify(triggerComponent, times(1)).findAllUnprocessed();
		verify(basicDAO, times(1)).selectEntityList(any(), eq(sqlQuery));
	}

	@Test
	public void testScheduler() {
		// call test method
		triggerServiceTest.scheduler();

		// verify invocations
		verify(triggerComponent, times(1)).findAllUnprocessed();
		verify(basicDAO, atLeastOnce()).selectEntityList(any(), anyString());
	}

	@Test
	public void testDetermineOperationFromTimestamp() {
		// prepare test data
		final Timestamp lastSync = Timestamp.valueOf(LocalDateTime.of(2024, 1, 3, 0, 0));

		final AltsystemDO shouldBeCreated = createRawAltsystemDO(
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 4, 0, 0)),
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 4, 0, 0))
		);

		final AltsystemDO shouldBeUpdated = createRawAltsystemDO(
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 0)),
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 4, 0, 0))
		);

		final AltsystemDO shouldBeLeftUntouched = createRawAltsystemDO(
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 0)),
				Timestamp.valueOf(LocalDateTime.of(2024, 1, 2, 0, 0))
		);

		// assert results
		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeCreated, lastSync))
				.isEqualTo(TriggerChangeOperation.CREATE);

		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeUpdated, lastSync))
				.isEqualTo(TriggerChangeOperation.UPDATE);

		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeLeftUntouched, lastSync))
				.isEqualTo(null);

		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeCreated, null))
				.isEqualTo(TriggerChangeOperation.CREATE);
		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeUpdated, null))
				.isEqualTo(TriggerChangeOperation.CREATE);
		Java6Assertions
				.assertThat(TriggerService.determineOperationFromTimestamp(shouldBeLeftUntouched, null))
				.isEqualTo(TriggerChangeOperation.CREATE);
	}
	@Test
	public void testGetMigrationTimestamp(){

		// configure mocks
		Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
		MigrationTimestampBE migrationTimestampBE = new MigrationTimestampBE();
		migrationTimestampBE.setSyncTimestamp(expectedTimestamp);
		List<MigrationTimestampBE> timestampList = new ArrayList<>();

		// call test method
		final Timestamp actualFalse = triggerServiceTest.getMigrationTimestamp();

		Java6Assertions.assertThat(actualFalse)
				.isNull();

		timestampList.add(migrationTimestampBE);

		// create stub
		when(migrationTimestampDAO.findAll()).thenReturn(timestampList);

		// call test method
		final Timestamp actual = triggerServiceTest.getMigrationTimestamp();

		Java6Assertions.assertThat(actual)
				.isNotNull();
	}
	@Test
	public void testSetMigrationTimestamp(){
		
		// configure mocks
		Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
		MigrationTimestampBE migrationTimestampBE = new MigrationTimestampBE();
		migrationTimestampBE.setSyncTimestamp(expectedTimestamp);
		List<MigrationTimestampBE> timestampList = new ArrayList<>();

		// call test method
		triggerServiceTest.setMigrationTimestamp(expectedTimestamp);

		verify(migrationTimestampDAO, times(1)).create(any());

		timestampList.add(migrationTimestampBE);

		// create stub
		when(migrationTimestampDAO.findAll()).thenReturn(timestampList);

		// call test method
		triggerServiceTest.setMigrationTimestamp(expectedTimestamp);

		verify(migrationTimestampDAO, times(1)).update(any());
	}
	@Test
	public void testCheckForMaliciousQueryParams(){
		String nullOffset = null;
		String nullLimit = null;
		String falseOffset = "HACKER";
		String falseLimit = "HACKER";
		String falseDateInterval = "HACKER";
		String falseDateIntervalTwo = "1 HACKER";
		String falseDateIntervalThree = "29 HACKER";
		String workingOffset = "0";
		String workingLimit = "500";
		String workingDateInterval = "1 MONTH";
		String falseDateIntervalFour = "10 YEAR";

		// Verify the behavior and the return values
		assert !triggerServiceTest.checkForMaliciousQueryParams(nullOffset, workingLimit, workingDateInterval);
		assert !triggerServiceTest.checkForMaliciousQueryParams(workingOffset, nullLimit, workingDateInterval);
		assert !triggerServiceTest.checkForMaliciousQueryParams(falseOffset, workingLimit, workingDateInterval);
		assert !triggerServiceTest.checkForMaliciousQueryParams(workingOffset, falseLimit, workingDateInterval);
		assert triggerServiceTest.checkForMaliciousQueryParams(workingOffset, workingLimit, workingDateInterval);
	}
	@Test
	public void testCheckForMaliciousDeletionParams(){
		String nullStatus= null;
		String nullDateInterval = null;
		String falseStatus = "HACKER";
		String falseDateInterval = "HACKER";
		String workingStatus = "Alle";
		String workingDateInterval = "1 MONTH";

		// Verify the behavior and the return values
		assert !triggerServiceTest.checkForMaliciousDeletionParams(nullStatus, workingDateInterval);
		assert !triggerServiceTest.checkForMaliciousDeletionParams(falseStatus, workingDateInterval);
		assert triggerServiceTest.checkForMaliciousDeletionParams(workingStatus, workingDateInterval);
	}
}
