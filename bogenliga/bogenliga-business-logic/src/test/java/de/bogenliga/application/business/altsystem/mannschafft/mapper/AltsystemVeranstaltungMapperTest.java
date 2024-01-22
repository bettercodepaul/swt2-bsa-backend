package de.bogenliga.application.business.altsystem.mannschafft.mapper;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test for AltsystemVeranstaltungMapper
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVeranstaltungMapperTest {

    // Constants for Mock Data
    private static final long CURRENTUSERID = 1L;
    private static final int LIGAID = 2;
    private static final long SPORTJAHR = 2022L;
    private static final int SPORTJAHRID = 4;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    private LigaComponent ligaComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;
    @Mock
    private UserComponent userComponent;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Mock
    private WettkampfTypComponent wettkampfTypComponent;

    @InjectMocks
    private AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;

    @Test
    public void testGetOrCreateVeranstaltung() {
        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO(anyInt(), anyInt(), anyString(), anyInt());
        altsystemMannschaftDO.setLiga_id(LIGAID);
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungLigaID((long)LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong()))
                .thenReturn(new VeranstaltungDO());

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        doNothing().when(altsystemUebersetzung).updateOrInsertUebersetzung(any(), any(), any(), any());

        // Mock behavior of groesse


        // call test method
        VeranstaltungDO actual = new VeranstaltungDO();
        actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());


    }

    @Test
    public void testTryCatch() {

        // prepare test data
        // altsystem MannschaftDO
        AltsystemMannschaftDO altsystemMannschaftDO = new AltsystemMannschaftDO(anyInt(), anyInt(), anyString(), anyInt());
        altsystemMannschaftDO.setLiga_id(LIGAID);
        altsystemMannschaftDO.setSaison_id(SPORTJAHRID);
        // expected Result
        VeranstaltungDO expectedVeranstaltungDO = new VeranstaltungDO();
        expectedVeranstaltungDO.setVeranstaltungLigaID((long)LIGAID);
        expectedVeranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr() throws Exception
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong())).thenThrow(new Exception("Test Exception"));

        // Mock behavior of altsystemVeranstaltungMapper.createVeranstaltung
        when(altsystemVeranstaltungMapper.createVeranstaltung(anyLong(), anyLong(), anyLong())).thenReturn(new VeranstaltungDO());

        // Call test method
        VeranstaltungDO actual = new VeranstaltungDO();
        actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());

    }
    /*
    @Test
    public void createVeranstaltung() {



    }
*/
}
