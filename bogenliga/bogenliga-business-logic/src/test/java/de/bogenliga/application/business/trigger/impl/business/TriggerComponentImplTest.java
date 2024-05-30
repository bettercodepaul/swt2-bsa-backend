package de.bogenliga.application.business.trigger.impl.business;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.MigrationTimestampDAO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerCountDAO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerCountDAOTest;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.business.trigger.impl.entity.MigrationTimestampBE;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * This class tests the TriggerComponentImpl. class
 *
 * @author Lino Cortese, eGGcellent problems nonadvisory & bloatware gmbh
 */
public class TriggerComponentImplTest {

	private static final Long TRIGGER_ID = 123L;
	private static final String TRIGGER_KATEGORIE = "testcat";
	private static final Long TRIGGER_ALTSYSTEMID = 234L;
	private static final TriggerChangeOperation TRIGGER_OPERATION = null;
	private static final TriggerChangeStatus TRIGGER_STATUS = null;
	private static final String TRIGGER_NACHRICHT = "testmsg";
	private static final Timestamp TRIGGER_RUNATUTC = null;
	private static final Timestamp TRIGGER_SYNCTIMESTAMP = null;
	private static final Long TRIGGERCOUNT_COUNT = 40000L;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private TriggerDAO triggerDAO;
	@Mock
	private TriggerCountDAO triggerCountDAO;
	@InjectMocks
	private TriggerComponentImpl underTest;


	public static MigrationTimestampBE getMigrationTimestampBE() {
		final MigrationTimestampBE expectedBE = new MigrationTimestampBE();
		expectedBE.setSyncTimestamp(TRIGGER_SYNCTIMESTAMP);
		expectedBE.setId(TRIGGER_ID);

		return expectedBE;
	}

	public static TriggerBE getTriggerBE() {
		final TriggerBE expectedBE = new TriggerBE();
		expectedBE.setId(TRIGGER_ID);
		expectedBE.setKategorie(TRIGGER_KATEGORIE);
		expectedBE.setAltsystemId(TRIGGER_ALTSYSTEMID);
		expectedBE.setChangeOperation(TRIGGER_OPERATION);
		expectedBE.setChangeStatus(TRIGGER_STATUS);
		expectedBE.setNachricht(TRIGGER_NACHRICHT);
		expectedBE.setRunAtUtc(TRIGGER_RUNATUTC);

		return expectedBE;
	}


	public static final OffsetDateTime TRIGGER_CREATEDATUTCO = null;
	public static final OffsetDateTime TRIGGER_RUNATUTCO = null;
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
	public static TriggerCountDO getTriggerCountDO(){
		return new TriggerCountDO(TRIGGERCOUNT_COUNT);
	}
	public static TriggerCountBE getTriggerCountBE(){
		final TriggerCountBE triggerCountBE = new TriggerCountBE();
		triggerCountBE.setCount(TRIGGERCOUNT_COUNT);
		return triggerCountBE;
	}

	@Test
	public void findAll() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();
		final List<TriggerBE> expectedBEList = Collections.singletonList(expectedBE);

		// configure mocks
		when(triggerDAO.findAll()).thenReturn(expectedBEList);

		// call test method
		final List<TriggerDO> actual = underTest.findAll();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();

		assertThat(actual.get(0).getId())
				.isEqualTo(expectedBE.getId());
		assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedBE.getKategorie());
		assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedBE.getAltsystemId());
		assertThat(actual.get(0).getOperation())
				.isEqualTo(expectedBE.getChangeOperation());
		assertThat(actual.get(0).getStatus())
				.isEqualTo(expectedBE.getChangeStatus());
		assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedBE.getNachricht());
		assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedBE.getCreatedAtUtc());
		assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedBE.getRunAtUtc());

		// verify invocations
		verify(triggerDAO, times(1)).findAll();
	}
	@Test
	public void findAllLimited() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();
		final List<TriggerBE> expectedBEList = Collections.singletonList(expectedBE);

		// configure mocks
		when(triggerDAO.findAllLimited()).thenReturn(expectedBEList);

		// call test method
		final List<TriggerDO> actual = underTest.findAllLimited();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();

		assertThat(actual.get(0).getId())
				.isEqualTo(expectedBE.getId());
		assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedBE.getKategorie());
		assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedBE.getAltsystemId());
		assertThat(actual.get(0).getOperation())
				.isEqualTo(expectedBE.getChangeOperation());
		assertThat(actual.get(0).getStatus())
				.isEqualTo(expectedBE.getChangeStatus());
		assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedBE.getNachricht());
		assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedBE.getCreatedAtUtc());
		assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedBE.getRunAtUtc());

		// verify invocations
		verify(triggerDAO, times(1)).findAllLimited();
	}
	@Test
	public void findAllUnprocessed() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();
		final List<TriggerBE> expectedBEList = Collections.singletonList(expectedBE);

		// configure mocks
		when(triggerDAO.findAllUnprocessed()).thenReturn(expectedBEList);

		// call test method
		final List<TriggerDO> actual = underTest.findAllUnprocessed();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();

		assertThat(actual.get(0).getId())
				.isEqualTo(expectedBE.getId());
		assertThat(actual.get(0).getKategorie())
				.isEqualTo(expectedBE.getKategorie());
		assertThat(actual.get(0).getAltsystemId())
				.isEqualTo(expectedBE.getAltsystemId());
		assertThat(actual.get(0).getOperation())
				.isEqualTo(expectedBE.getChangeOperation());
		assertThat(actual.get(0).getStatus())
				.isEqualTo(expectedBE.getChangeStatus());
		assertThat(actual.get(0).getNachricht())
				.isEqualTo(expectedBE.getNachricht());
		assertThat(actual.get(0).getCreatedAtUtc())
				.isEqualTo(expectedBE.getCreatedAtUtc());
		assertThat(actual.get(0).getRunAtUtc())
				.isEqualTo(expectedBE.getRunAtUtc());

		// verify invocations
		verify(triggerDAO, times(1)).findAllUnprocessed();
	}

	@Test
	public void findAllCount() {
		// prepare test data
		final TriggerCountBE expectedBE = getTriggerCountBE();

		// configure mocks
		when(triggerCountDAO.findAllCount()).thenReturn(expectedBE);


		// call test method
		final TriggerCountDO actual = underTest.findAllCount();

		// assert result
		assertThat(actual)
				.isNotNull();

		assertThat(actual.getCount())
				.isEqualTo(expectedBE.getCount());

		// verify invocations
		verify(triggerCountDAO, times(1)).findAllCount();
	}
	@Test
	public void findUnprocessedCount() {
		// prepare test data
		final TriggerCountBE expectedBE = getTriggerCountBE();

		// configure mocks
		when(triggerCountDAO.findUnprocessedCount()).thenReturn(expectedBE);

		// call test method
		final TriggerCountDO actual = underTest.findUnprocessedCount();

		// assert result
		assertThat(actual)
				.isNotNull();

		assertThat(actual.getCount())
				.isEqualTo(expectedBE.getCount());

		// verify invocations
		verify(triggerCountDAO, times(1)).findUnprocessedCount();
	}



}
