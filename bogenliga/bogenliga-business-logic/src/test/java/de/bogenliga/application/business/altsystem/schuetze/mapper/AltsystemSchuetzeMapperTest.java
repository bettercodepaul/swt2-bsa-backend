package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.SQLException;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDAO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AltsystemSchuetzeMapperTest {

    private static final Long Id = 1L;



    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;

    @Mock
    private AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    @Mock
    private DsbMitgliedDO dsbMitgliedDO;

    @InjectMocks
    private AltsystemSchuetzeMapper altsystemSchuetzeMapper;


    @Test
    public void testToDO() throws SQLException {
        // prepare test data
        // altsystem DO
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        altsystemSchuetzeDO.setRuecknr(1);
        altsystemSchuetzeDO.setName("Bammert, Marco");
        altsystemSchuetzeDO.setMannschaft_id(387);

        // expectedResult
        DsbMitgliedDO expectedDO = new DsbMitgliedDO();
        expectedDO.setVorname("Marco");
        expectedDO.setNachname("Bammert");

        // call test method
        DsbMitgliedDO actual = new DsbMitgliedDO();
        actual = altsystemSchuetzeMapper.toDO(actual, altsystemSchuetzeDO);

        // assert result
        assertThat(actual.getVorname()).isEqualTo(expectedDO.getVorname());
        assertThat(actual.getNachname()).isEqualTo(expectedDO.getNachname());

    }
}
