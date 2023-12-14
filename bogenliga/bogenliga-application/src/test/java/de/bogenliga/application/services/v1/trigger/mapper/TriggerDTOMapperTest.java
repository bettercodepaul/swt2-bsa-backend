package de.bogenliga.application.services.v1.trigger.mapper;

import org.junit.Test;
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
	private static final Long VERSION = 3L;
	private static final String TIMESTAMP = "12.12";
	private static final String DESCRIPTION = "sampleData";
	private static final String STATUS = "bad";


	private TriggerDTO getDTO(){
		return new TriggerDTO(ID, VERSION, TIMESTAMP, DESCRIPTION, STATUS);
	}

	private TriggerDO getDO(){
		return new TriggerDO(ID, VERSION, TIMESTAMP, DESCRIPTION, STATUS);
	}


	@Test
	public void toDOTest(){
		TriggerDTO triggerDTO = getDTO();
		final TriggerDO actual = TriggerDTOMapper.toDO.apply(triggerDTO);
		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getVersion()).isEqualTo(VERSION);
		assertThat(actual.getTimestamp()).isEqualTo(TIMESTAMP);
		assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
		assertThat(actual.getStatus()).isEqualTo(STATUS);
	}

	@Test
	public void toDTOTest(){
		TriggerDO triggerDO = getDO();
		final TriggerDTO actual = TriggerDTOMapper.toDTO.apply(triggerDO);
		assertThat(actual.getId()).isEqualTo(ID);
		assertThat(actual.getVersion()).isEqualTo(VERSION);
		assertThat(actual.getTimestamp()).isEqualTo(TIMESTAMP);
		assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
		assertThat(actual.getStatus()).isEqualTo(STATUS);
	}

}
