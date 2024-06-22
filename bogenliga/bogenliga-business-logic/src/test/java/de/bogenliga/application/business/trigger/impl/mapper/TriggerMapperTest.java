package de.bogenliga.application.business.trigger.impl.mapper;

import java.sql.Timestamp;
import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
import de.bogenliga.application.common.time.DateProvider;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerCountBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerCountDO;
import static org.assertj.core.api.Assertions.assertThat;
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
	private static final Timestamp CREATED_AT_UTC = null;
	private static final Timestamp RUN_AT_UTC = null;
	private static final Long count = 40000L;
	private static final Long altCount =50000L;


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
		assertThat(actual.getCreatedAt()).isEqualTo(DateProvider.convertTimestamp(CREATED_AT_UTC));
		assertThat(actual.getRunAtUtc()).isEqualTo(DateProvider.convertTimestamp(RUN_AT_UTC));
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
		assertThat(actual.getCreatedAtUtc()).isEqualTo(CREATED_AT_UTC);
		assertThat(actual.getRunAtUtc()).isEqualTo(RUN_AT_UTC);
	}

	@Test
	public void toCountBE() throws Exception{
		final TriggerCountDO triggerCountDO = getTriggerCountDO();

		final TriggerCountBE actual = TriggerMapper.toTriggerCountBE.apply(triggerCountDO);

		assertThat(actual.getCount()).isEqualTo(count);
	}

	@Test
	public void toCountDO() throws Exception{
		final TriggerCountBE triggerCountBE = getTriggerCountBE();

		final TriggerCountDO actual = TriggerMapper.toTriggerCountDO.apply(triggerCountBE);

		assertThat(actual.getCount()).isEqualTo(count);
	}

}
