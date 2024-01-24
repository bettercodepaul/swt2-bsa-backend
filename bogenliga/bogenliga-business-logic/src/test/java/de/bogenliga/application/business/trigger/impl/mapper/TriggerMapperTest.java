package de.bogenliga.application.business.trigger.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import static org.assertj.core.api.Assertions.assertThat;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerDO;


/**
 * Tests the TriggerMapper class
 *
 * @author Lino Cortese, nocturnal activity coding & caffeine gmbh
 */
public class TriggerMapperTest {

	private static final Long ID = 123L;
	private static final String KATEGORIE = "testcat";
	private static final Long ALTSYSTEM_ID = 234L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "testmsg";
	private static final Timestamp RUN_AT_UTC = null;


	@Test
	public void toDO() throws Exception {
		final TriggerBE triggerBE = getTriggerBE();

		final TriggerDO actual = TriggerMapper.toTriggerDO.apply(triggerBE);

		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getKategorie()).isEqualTo(KATEGORIE);
		assertThat(actual.getAltsystemId()).isEqualTo(ALTSYSTEM_ID);
		assertThat(actual.getOperation()).isEqualTo(OPERATION);
		assertThat(actual.getStatus()).isEqualTo(STATUS);
		assertThat(actual.getNachricht()).isEqualTo(NACHRICHT);
		assertThat(actual.getRunAtUtc()).isEqualTo(RUN_AT_UTC);
	}

	@Test
	public void toBE() throws Exception {
		final TriggerDO triggerDO = getTriggerDO();

		final TriggerBE actual = TriggerMapper.toTriggerBE.apply(triggerDO);

		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getKategorie()).isEqualTo(KATEGORIE);
		assertThat(actual.getAltsystemId()).isEqualTo(ALTSYSTEM_ID);
		assertThat(actual.getChangeOperation()).isEqualTo(OPERATION);
		assertThat(actual.getChangeStatus()).isEqualTo(STATUS);
		assertThat(actual.getNachricht()).isEqualTo(NACHRICHT);
		assertThat(actual.getRunAtUtc()).isEqualTo(RUN_AT_UTC);
	}

}
