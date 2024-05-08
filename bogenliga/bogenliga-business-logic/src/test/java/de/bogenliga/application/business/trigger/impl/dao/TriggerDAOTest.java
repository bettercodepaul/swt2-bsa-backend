package de.bogenliga.application.business.trigger.impl.dao;

import java.sql.Timestamp;
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
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.entity.RawTriggerBE;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.impl.mapper.TriggerMapper;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getErrorTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getInProgressTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getNewTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getSuccessTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the TriggerDAO class
 *
 * @author Lino Cortese, absolutely no solutions testing & failing gmbh
 */
public class TriggerDAOTest {

	private static final Long ID = 5138008L;
	private static final String KATEGORIE = "It's fokin RAW";
	private static final Long ALTSYSTEM_ID = 1353L;
	private static final Long OPERATION = 1L;
	private static final Long STATUS = 2L;
	private static final String NACHRICHT = "You donut";
	private static final Timestamp RUN_AT_UTC = null;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private BasicDAO basicDAO;
	@Mock
	private TriggerBE triggerBE;
	@Mock
	private RawTriggerBE rawTriggerBE;
	@InjectMocks
	private TriggerDAO triggerDAO;

	public static RawTriggerBE getRawTriggerBE() {
		RawTriggerBE raw = new RawTriggerBE();
		raw.setId(ID);
		raw.setKategorie(KATEGORIE);
		raw.setAltsystemId(ALTSYSTEM_ID);
		raw.setChangeOperationId(OPERATION);
		raw.setChangeStatusId(STATUS);
		raw.setNachricht(NACHRICHT);
		raw.setRunAtUtc(RUN_AT_UTC);
		return raw;
	}

	@Test
	public void testFindAll() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findAll();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}

	@Test
	public void testResolveRawTrigger() {
		// prepare test data
		final RawTriggerBE rawTriggerBE = getRawTriggerBE();

		// call test method
		TriggerBE actual = triggerDAO.resolveRawTrigger(rawTriggerBE);

		// assert result
		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getKategorie()).isEqualTo(KATEGORIE);
		assertThat(actual.getAltsystemId()).isEqualTo(ALTSYSTEM_ID);
		assertThat(actual.getChangeOperationId()).isEqualTo(OPERATION);
		assertThat(actual.getChangeStatusId()).isEqualTo(STATUS);
		assertThat(actual.getNachricht()).isEqualTo(NACHRICHT);
		assertThat(actual.getRunAtUtc()).isEqualTo(RUN_AT_UTC);
		assertThat(actual.getChangeStatus()).isEqualTo(TriggerChangeStatus.parse(STATUS));
		assertThat(actual.getChangeOperation()).isEqualTo(TriggerChangeOperation.parse(OPERATION));
	}

	@Test
	public void testFindAllLimited() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findAllLimited();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllUnprocessed() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findAllUnprocessed();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllErrors() {
		// prepare test data
		final TriggerBE expectedBE = getErrorTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findErrors("0","500", "1 MONTH");

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllNews() {
		// prepare test data
		final TriggerBE expectedBE = getNewTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findNews("0","500", "1 MONTH");

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllSuccess() {
		// prepare test data
		final TriggerBE expectedBE = getSuccessTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findSuccessed("0","500", "1 MONTH");

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllInProgress() {
		// prepare test data
		final TriggerBE expectedBE = getInProgressTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findInProgress("0","500", "1 MONTH");

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
	@Test
	public void testFindAllWithPages() {
		// prepare test data
		final TriggerBE expectedBE = getTriggerBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<TriggerBE> actual = triggerDAO.findAllWithPages("0","500", "1 MONTH");

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}
}
