package de.bogenliga.application.business.competitionclass.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;

import java.util.Calendar;

import static de.bogenliga.application.business.competitionclass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassBE;
import static de.bogenliga.application.business.competitionclass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Leila Taraman, Leila.Taraman@Student.Reutlingen-University.DE
 */
public class CompetitionClassMapperTest {

    private static final long USER = 0L;

    private static final long ID = 1337L;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEALTERMIN = 44L;
    private static final long KLASSEALTERMAX = 66L;
    private static final long KLASSEJAHRGANGMIN = 1998L;
    private static final long KLASSEJAHRGANGMAX = 1996L;
    private static final long KLASSENR = 2L;

    int year = Calendar.getInstance().get(Calendar.YEAR);

    @Test
    public void toVO() throws Exception{

        final CompetitionClassBE competitionClassBE = getCompetitionClassBE();

        final CompetitionClassDO actual = CompetitionClassMapper.toCompetitionClassDO.apply(competitionClassBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getKlasseName()).isEqualTo(KLASSENAME);
        assertThat(actual.getKlasseJahrgangMax()).isEqualTo(year - KLASSEALTERMAX);
        assertThat(actual.getKlasseJahrgangMin()).isEqualTo(year - KLASSEALTERMIN);
        assertThat(actual.getKlasseNr()).isEqualTo(KLASSENR);

    }

    @Test
    public void toBE() throws Exception{

        final CompetitionClassDO competitionClassDO = getCompetitionClassDO();

        final CompetitionClassBE actual = CompetitionClassMapper.toCompetitionClassBE.apply(competitionClassDO);

        assertThat(actual.getKlasseId()).isEqualTo(ID);
        assertThat(actual.getKlasseName()).isEqualTo(KLASSENAME);
        assertThat(actual.getKlasseAlterMax()).isEqualTo(year - KLASSEJAHRGANGMAX);
        assertThat(actual.getKlasseAlterMin()).isEqualTo(year - KLASSEJAHRGANGMIN);
        assertThat(actual.getKlasseNr()).isEqualTo(KLASSENR);

    }

}
