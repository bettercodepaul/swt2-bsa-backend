package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import java.util.LinkedList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests the mapping functionality from Data of the legacy system
 * to the "Wettkampf"-Entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemWettkampftagMapperTest {

    private static final long CURRENTUSERID = 10L;
    private static final long VERANSTALTUNG_ID = 2L;
    private static final long WETTKAMPFTYP_ID = 5L;
    private static final long SPORTJAHR = 2030;
    private static final long LIGA_ID = 6L;
    private static final long DISZIPLIN_ID = 1L;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    WettkampfComponent wettkampfComponent;
    @Mock
    VeranstaltungComponent veranstaltungComponent;
    @Mock
    LigaComponent ligaComponent;
    @Mock
    AltsystemUebersetzung altsystemUebersetzung;

    @InjectMocks
    AltsystemWettkampftagMapper altsystemWettkampftagMapper;

    public List<WettkampfDO> getMockWettkampftage(){
        List<WettkampfDO> wettkaempfe = new LinkedList<>();
        for(long i = 1L; i <= 4; i++){
            WettkampfDO wettkampf = new WettkampfDO();
            wettkampf.setId(i);
            wettkaempfe.add(wettkampf);
        }
        return wettkaempfe;
    }

    public VeranstaltungDO getMockVeranstaltung(){
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungID(VERANSTALTUNG_ID);
        veranstaltungDO.setVeranstaltungWettkampftypID(WETTKAMPFTYP_ID);
        veranstaltungDO.setVeranstaltungSportJahr(SPORTJAHR);
        veranstaltungDO.setVeranstaltungLigaID(LIGA_ID);
        return veranstaltungDO;
    }

    public LigaDO getMockLiga(){
        LigaDO ligaDO = new LigaDO();
        ligaDO.setId(LIGA_ID);
        ligaDO.setDisziplinId(DISZIPLIN_ID);
        return ligaDO;
    }

    @Test
    public void testGetWettkampftage() {
        // prepare test data
        // altsystem WettkampfdatenDO
        AltsystemWettkampfdatenDO altsystemWettkampfdatenDO = new AltsystemWettkampfdatenDO();
        altsystemWettkampfdatenDO.setMannschaft(2L);
        // liga uebersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setBogenligaId(VERANSTALTUNG_ID);

        // expected Result
        List<WettkampfDO> wettkaempfe = getMockWettkampftage();

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung), anyLong()))
                .thenReturn(mannschaftUebersetzung);

        // Mock behavior of wettkampfComponent.findByLigaIDAndSportjahr
        when(wettkampfComponent.findAllByVeranstaltungId(VERANSTALTUNG_ID))
                .thenReturn(wettkaempfe);

        // call test method
        List<WettkampfDO> actual = altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemWettkampfdatenDO, CURRENTUSERID);

        // assert result
        assertThat(actual).isEqualTo(wettkaempfe);
    }

    @Test
    public void testConditionCreateWettkampftage() {
        AltsystemWettkampftagMapper altsystemWettkampftagMapper1 = Mockito.spy(altsystemWettkampftagMapper);

        // prepare test data
        // altsystem WettkampfdatenDO
        AltsystemWettkampfdatenDO altsystemWettkampfdatenDO = new AltsystemWettkampfdatenDO();
        altsystemWettkampfdatenDO.setMannschaft(2L);
        // liga uebersetzung
        AltsystemUebersetzungDO mannschaftUebersetzung = new AltsystemUebersetzungDO();
        mannschaftUebersetzung.setBogenligaId(VERANSTALTUNG_ID);

        // expected Result
        List<WettkampfDO> wettkaempfe = getMockWettkampftage();

        // Mock behavior of AltsystemUebersetzung.findByAltsystemID
        when(altsystemUebersetzung.findByAltsystemID(eq(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung), anyLong()))
                .thenReturn(mannschaftUebersetzung);

        // Mock behavior of wettkampfComponent.findByLigaIDAndSportjahr
        when(wettkampfComponent.findAllByVeranstaltungId(VERANSTALTUNG_ID))
                .thenReturn(new LinkedList<>());

        // Mock behavior of wettkampfComponent.findByLigaIDAndSportjahr
        doReturn(wettkaempfe).when(altsystemWettkampftagMapper1).createWettkampftage(VERANSTALTUNG_ID, CURRENTUSERID);

        // call test method
        List<WettkampfDO> actual = altsystemWettkampftagMapper1.getOrCreateWettkampftage(altsystemWettkampfdatenDO, CURRENTUSERID);

        // assert result
        assertThat(actual).isEqualTo(wettkaempfe);
    }

    @Test
    public void testCreateWettkampftage() {
        // prepare test data
        VeranstaltungDO veranstaltungDO = getMockVeranstaltung();
        LigaDO ligaDO = getMockLiga();

        // Mock behavior of veranstaltungComponent.findById
        doReturn(veranstaltungDO).when(veranstaltungComponent).findById(veranstaltungDO.getVeranstaltungID());

        // Mock behavior of ligaComponent.findById
        doReturn(ligaDO).when(ligaComponent).findById(veranstaltungDO.getVeranstaltungLigaID());

        // Mock db access
        doAnswer(invocation -> invocation.getArgument(0)).when(wettkampfComponent).create(any(), anyLong());

        // call test method
        List<WettkampfDO> actual = altsystemWettkampftagMapper.createWettkampftage(VERANSTALTUNG_ID, CURRENTUSERID);

        // assert result / general properties
        for (int i = 0; i < 4; i++){
            WettkampfDO currentWettkampf = actual.get(i);
            assertThat(currentWettkampf.getWettkampfTag()).isEqualTo(i + 1);
            assertThat(currentWettkampf.getWettkampfAusrichter()).isEqualTo(CURRENTUSERID);
            assertThat(currentWettkampf.getWettkampfDisziplinId()).isEqualTo(DISZIPLIN_ID);
            assertThat(currentWettkampf.getWettkampfVeranstaltungsId()).isEqualTo(VERANSTALTUNG_ID);
            assertThat(currentWettkampf.getWettkampfTypId()).isEqualTo(WETTKAMPFTYP_ID);
        }

        // Termine
        assertThat(actual.get(0).getWettkampfDatum()).isEqualTo((SPORTJAHR - 1) + "-11-01");
        assertThat(actual.get(1).getWettkampfDatum()).isEqualTo((SPORTJAHR - 1) + "-12-01");
        assertThat(actual.get(2).getWettkampfDatum()).isEqualTo(SPORTJAHR + "-01-01");
        assertThat(actual.get(3).getWettkampfDatum()).isEqualTo(SPORTJAHR + "-02-01");



    }

}