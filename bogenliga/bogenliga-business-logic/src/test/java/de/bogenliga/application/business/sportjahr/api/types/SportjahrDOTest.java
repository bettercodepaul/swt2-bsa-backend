package de.bogenliga.application.business.sportjahr.api.types;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Die SportjahrDO Klasse wird hier getestet.
 *
 * @author Emanuel Petrinovic
 */
public class SportjahrDOTest {

    private static final long ID = 42;
    private static final long SPORTJAHR = 2000;
    private static final long VERSION = 7;

    private SportjahrDO getDO()
    {
        SportjahrDO sportjahrDO = new SportjahrDO();
        sportjahrDO.setId(ID);
        sportjahrDO.setSportjahr(SPORTJAHR);
        sportjahrDO.setVersion(VERSION);
        return sportjahrDO;
    }

    @Test
    public void toDO()
    {
        SportjahrDO sportjahrDO = getDO();
        assertThat(sportjahrDO.getId()).isEqualTo(ID);
        assertThat(sportjahrDO.getSportjahr()).isEqualTo(SPORTJAHR);
        assertThat(sportjahrDO.getVersion()).isEqualTo(VERSION);
    }

}