package de.bogenliga.application.services.v1.trigger.mapper;

import java.time.OffsetDateTime;
import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the TriggerDTOMapper class
 *
 */
public class TriggerDTOMapperTest{

	//Test data
	private static final Long ID = 24L;
	private static final String KATEGORIE = "needles";
	private static final Long ALTSYSTEM_ID = 25L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "too bad!";
	private static final OffsetDateTime CREATED_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime RUN_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime LAST_MODIFIED_AT_UTC = OffsetDateTime.MAX;


	private TriggerDTO getDTO(){
		return new TriggerDTO(ID, KATEGORIE, ALTSYSTEM_ID, OPERATION, STATUS, NACHRICHT, CREATED_AT_UTC, RUN_AT_UTC, LAST_MODIFIED_AT_UTC);
	}

	private TriggerDO getDO(){
		return new TriggerDO(ID, KATEGORIE, ALTSYSTEM_ID, OPERATION, STATUS, NACHRICHT, CREATED_AT_UTC,
                LAST_MODIFIED_AT_UTC);
	}


	@Test
	public void toDOTest(){
		TriggerDTO triggerDTO = getDTO();
		final TriggerDO actual = TriggerDTOMapper.toDO.apply(triggerDTO);
		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getKategorie()).isEqualTo(KATEGORIE);
		assertThat(actual.getAltsystemId()).isEqualTo(ALTSYSTEM_ID);
		assertThat(actual.getOperation()).isEqualTo(OPERATION);
		assertThat(actual.getStatus()).isEqualTo(STATUS);
		assertThat(actual.getNachricht()).isEqualTo(NACHRICHT);
		assertThat(actual.getCreatedAtUtc()).isEqualTo(CREATED_AT_UTC);
	}

	@Test
	public void toDTOTest(){
		TriggerDO triggerDO = getDO();
		final TriggerDTO actual = TriggerDTOMapper.toDTO.apply(triggerDO);
		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getKategorie()).isEqualTo(KATEGORIE);
		assertThat(actual.getAltsystemId()).isEqualTo(ALTSYSTEM_ID);
		assertThat(actual.getOperation()).isEqualTo(OPERATION);
		assertThat(actual.getStatus()).isEqualTo(STATUS);
		assertThat(actual.getNachricht()).isEqualTo(NACHRICHT);
		assertThat(actual.getCreatedAtUtc()).isEqualTo(CREATED_AT_UTC);
		assertThat(actual.getlastModifiedAtUtc()).isEqualTo(LAST_MODIFIED_AT_UTC);
	}

}
