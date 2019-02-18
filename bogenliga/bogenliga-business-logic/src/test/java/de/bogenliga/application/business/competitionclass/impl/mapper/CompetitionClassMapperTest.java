package de.bogenliga.application.business.competitionClass.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.business.competitionclass.impl.mapper.CompetitionClassMapper;
import static de.bogenliga.application.business.competitionClass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassBE;
import static de.bogenliga.application.business.competitionClass.impl.business.CompetitionClassComponentImplTest.getCompetitionClassDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Leila Taraman, Leila.Taraman@Student.Reutlingen-University.DE
 */
public class CompetitionClassMapperTest {

    private static final long USER = 0L;

    private static final long ID = 1337L;
    private static final String KLASSENAME = "Herren";
    private static final long KLASSEJAHRGANGMIN = 10L;
    private static final long KLASSEJAHRGANGMAX = 50L;
    private static final long KLASSENR = 42L;

    @Test
    public void toVO() throws Exception{

        final CompetitionClassBE competitionClassBE = getCompetitionClassBE();

        final CompetitionClassDO actual = CompetitionClassMapper.toCompetitionClassDO.apply(competitionClassBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getKlasseName()).isEqualTo(KLASSENAME);

    }

    @Test
    public void toBE() throws Exception{

        final CompetitionClassDO competitionClassDO = getCompetitionClassDO();

        final CompetitionClassBE actual = CompetitionClassMapper.toCompetitionClassBE.apply(competitionClassDO);

        assertThat(actual.getKlasseId()).isEqualTo(ID);
        assertThat(actual.getKlasseName()).isEqualTo(KLASSENAME);

    }

}