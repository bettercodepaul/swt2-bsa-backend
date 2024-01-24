package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;

/**
 * Tests the TriggerBE class
 *
 * @author Lino Cortese, insomnia solutions bed & sleep gmbh
 */
public class TriggerBETest {

	private static final Long ID = 123L;
	private static final String KATEGORIE = "sleep";
	private static final Long ALTSYSTEM_ID = 234L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "invalid";
	private static final Timestamp CREATED_AT_UTC = null;
	private static final Timestamp RUN_AT_UTC = null;

	@Test
	public void testToString() {
		final TriggerBE triggerBE = getTriggerBE();
		triggerBE.setId(ID);
		triggerBE.setKategorie(KATEGORIE);
		triggerBE.setAltsystemId(ALTSYSTEM_ID);
		triggerBE.setChangeOperation(OPERATION);
		triggerBE.setChangeStatus(STATUS);
		triggerBE.setNachricht(NACHRICHT);
		triggerBE.setRunAtUtc(CREATED_AT_UTC);
		triggerBE.setRunAtUtc(RUN_AT_UTC);

		final String actual = triggerBE.toString();

		assertThat(actual)
				.isNotEmpty()
				.contains(Long.toString(ID))
				.contains(KATEGORIE);
	}

}
