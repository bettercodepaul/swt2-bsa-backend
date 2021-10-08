package de.bogenliga.application.business.competitionclass.impl.entity;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static de.bogenliga.application.business.competitionclass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassBE;

/**
 * @author Leila Taraman, Leila.Taraman@Student.Reutlingen-University.DE
 */
public class CompetitionClassBETest {

    private static final long USER = 0L;

    private static final long ID = 42L;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEJAHRGANGMIN = 10L;
    private static final long KLASSEJAHRGANGMAX = 50L;
    private static final long KLASSENR = 1337L;


    @Test
    public void assertToString(){
        final CompetitionClassBE underTest = getCompetitionClassBE();
        underTest.setKlasseId(ID);
        underTest.setKlasseName(KLASSENAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(KLASSENAME);

    }

    @Test
    public void assertToStringWithOutKlasseName(){
        final CompetitionClassBE underTest = new CompetitionClassBE();
        underTest.setKlasseId(ID);
        underTest.setKlasseName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }

}