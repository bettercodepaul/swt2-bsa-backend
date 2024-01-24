package de.bogenliga.application.business.trigger.impl.dao;

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
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;

import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the TriggerDAO class
 *
 * @author Lino Cortese, absolutely no solutions testing & failing gmbh
 */
public class TriggerDAOTest {

	private static final Long ID = 4L;
	private static final String KATEGORIE = "wut";
	private static final Long ALTSYSTEM_ID = 5L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "wot";
	private static final OffsetDateTime RUN_AT_UTC = OffsetDateTime.MIN;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private BasicDAO basicDAO;
	@InjectMocks
	private TriggerDAO triggerDAO;

	@Test
	public void ftestFindAll() {
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

}
