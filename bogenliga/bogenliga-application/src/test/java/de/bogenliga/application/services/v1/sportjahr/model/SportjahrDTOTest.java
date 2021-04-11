package de.bogenliga.application.services.v1.sportjahr.model;

import org.junit.Test;
import de.bogenliga.application.services.v1.sportjahr.SportjahrDTO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Die SportjahrDTO Klasse wird hier getestet.
 *
 * @author Emanuel Petrinovic
 */
public class SportjahrDTOTest
{
    private static final long ID = 42;
    private static final long SPORTJAHR = 2000;
    private static final long VERSION = 7;

    private SportjahrDTO getDTO()
    {
        SportjahrDTO sportjahrDTO = new SportjahrDTO();
        sportjahrDTO.setId(ID);
        sportjahrDTO.setSportjahr(SPORTJAHR);
        sportjahrDTO.setVersion(VERSION);
        return sportjahrDTO;
    }

    @Test
    public void toDTO()
    {
        SportjahrDTO sportjahrDTO = getDTO();
        assertThat(sportjahrDTO.getId()).isEqualTo(ID);
        assertThat(sportjahrDTO.getSportjahr()).isEqualTo(SPORTJAHR);
        assertThat(sportjahrDTO.getVersion()).isEqualTo(VERSION);
    }

}
