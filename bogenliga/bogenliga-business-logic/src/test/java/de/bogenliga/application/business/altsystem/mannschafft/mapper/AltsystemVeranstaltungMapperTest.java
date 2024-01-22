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
    private AltsystemUebersetzungDO altsystemUebersetzungDO;

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
        when(AltsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Liga_Liga), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        when(AltsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Saison_Sportjahr), anyLong()))
                .thenReturn(new AltsystemUebersetzungDO());

        // Mock behavior of veranstaltungComponent.findByLigaIDAndSportjahr
        when(veranstaltungComponent.findByLigaIDAndSportjahr(anyLong(), anyLong()))
                .thenReturn(new VeranstaltungDO());

        // Mock behavior of AltsystemUebersetzung.updateOrInsertUebersetzung
        // when(AltsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung, anyInt(), anyInt(), anyString()));

        // call test method
        VeranstaltungDO actual = new VeranstaltungDO();
        actual = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemMannschaftDO, CURRENTUSERID);

        // assert result
        assertThat(actual.getVeranstaltungLigaID()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungLigaID());
        assertThat(actual.getVeranstaltungSportJahr()).isEqualTo(expectedVeranstaltungDO.getVeranstaltungSportJahr());

        //verify
        verify(veranstaltungComponent, times(1)).findByLigaIDAndSportjahr(anyLong(), anyLong());
        verify(altsystemVeranstaltungMapper, times(1)).createVeranstaltung(anyLong(), anyLong(), anyLong());
        // verify(AltsystemUebersetzung, times(1)).findByAltsystemID(AltsystemUebersetzungKategorie.Liga_Liga, anyLong());
        // verify(AltsystemUebersetzung, times(1)).updateOrInsertUebersetzung(any(), anyInt(), anyInt(), anyString());
    }

}
