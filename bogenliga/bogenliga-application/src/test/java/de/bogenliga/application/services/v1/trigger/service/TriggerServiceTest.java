package de.bogenliga.application.services.v1.trigger.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Java6Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.common.component.dao.BasicDAO;
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
	private static final String TRIGGER_NACHRICHT = "see";
	public static final OffsetDateTime TRIGGER_CREATEDATUTCO = null;
	public static final OffsetDateTime TRIGGER_RUNATUTCO = null;
	private LocalDateTime currentTime = LocalDateTime.now();

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private BasicDAO basicDao;
	@Mock
	private TriggerDAO triggerDAO;
	@Mock
	private TriggerComponent triggerComponent;
	@Mock
	private LigaComponent ligaComponent;
	@InjectMocks
	private TriggerService triggerServiceTest;


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

	@Test
	public void testFindAll() {
		// prepare test data
		final TriggerDO expectedDO = getTriggerDO();
		final List<TriggerDO> expectedDOList = Collections.singletonList(expectedDO);

		// configure mocks
		when(triggerComponent.findAll()).thenReturn(expectedDOList);

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
		verify(triggerComponent, times(1)).findAll();
	}



}
