package de.bogenliga.application.business.namemapping.impl.business;

import de.bogenliga.application.business.disziplin.impl.dao.DisziplinDAO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAOext;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAOext;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampfTypDAO;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameMappingComponentImplTest extends TestCase {

    @Mock
    private VereinDAOext vereinDAOext;
    @Mock
    private DsbMitgliedDAO dsbMitgliedDAO;
    @Mock
    private DsbMannschaftDAOext dsbMannschaftDAOext;
    @Mock
    private DisziplinDAO disziplinDAO;
     @Mock
    private VeranstaltungDAO veranstaltungDAO;
    @Mock
    private LigaDAO ligaDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private WettkampfTypDAO wettkampfTypDAO;
    @InjectMocks
    private NameMappingComponentImpl nameMappingComponent;

    @Before
    public void setUp() {
        vereinDAOext = mock(VereinDAOext.class);
        dsbMitgliedDAO = mock(DsbMitgliedDAO.class);
        dsbMannschaftDAOext = mock(DsbMannschaftDAOext.class);
        disziplinDAO = mock(DisziplinDAO.class);
        veranstaltungDAO = mock(VeranstaltungDAO.class);
        ligaDAO = mock(LigaDAO.class);
        userDAO = mock(UserDAO.class);
        wettkampfTypDAO = mock(WettkampfTypDAO.class);

        nameMappingComponent = new NameMappingComponentImpl(
                vereinDAOext, dsbMitgliedDAO, dsbMannschaftDAOext, disziplinDAO,
                veranstaltungDAO, ligaDAO, userDAO, wettkampfTypDAO
        );
    }

    @Test
    public void testGetDisziplinNameForDisziplinId() {
        DisziplinBE disziplin = mock(DisziplinBE.class);
        when(disziplin.getName()).thenReturn("Recurve");
        when(disziplinDAO.findById(1L)).thenReturn(disziplin);

        assertEquals("Recurve", nameMappingComponent.getDisziplinNameForDisziplinId(1L));
    }

    @Test
    public void testGetDsbMitgliedFullNameForDsbMitgliedId() {
        DsbMitgliedBE mitglied = mock(DsbMitgliedBE.class);
        when(mitglied.getDsbMitgliedVorname()).thenReturn("Max");
        when(mitglied.getDsbMitgliedNachname()).thenReturn("Mustermann");
        when(dsbMitgliedDAO.findById(2L)).thenReturn(mitglied);

        assertEquals("Max Mustermann", nameMappingComponent.getDsbMitgliedFullNameForDsbMitgliedId(2L));
    }

    @Test
    public void testGetLigaNameForLigaId() {
        LigaBE liga = mock(de.bogenliga.application.business.liga.impl.entity.LigaBE.class);
        when(liga.getLigaName()).thenReturn("Bundesliga");
        when(ligaDAO.findById(3L)).thenReturn(liga);

        assertEquals("Bundesliga", nameMappingComponent.getLigaNameForLigaId(3L));
    }

    @Test
    public void testGetMannschaftsnameForVereinIDandMannschaftNr() {
        VereinBEext verein = mock(VereinBEext.class);
        when(verein.getVereinName()).thenReturn("VereinX");
        when(vereinDAOext.findById(4L)).thenReturn(verein);

        assertEquals("VereinX 2", nameMappingComponent.getMannschaftsnameForVereinIDandMannschaftNr(4L, 2L));
        assertEquals("VereinX", nameMappingComponent.getMannschaftsnameForVereinIDandMannschaftNr(4L, 0L));
    }

    @Test
    public void testGetMannschaftsnameForMannschaftId() {
        DsbMannschaftBEext mannschaft = mock(DsbMannschaftBEext.class);
        when(mannschaft.getVereinName()).thenReturn("VereinY");
        when(mannschaft.getNummer()).thenReturn(1L);
        when(dsbMannschaftDAOext.findByIdwithName(5L)).thenReturn(mannschaft);

        assertEquals("VereinY 1", nameMappingComponent.getMannschaftsnameForMannschaftId(5L));
    }

    @Test
    public void testGetSportjahrForVeranstaltungsId() {
        VeranstaltungBE veranstaltung = mock(de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE.class);
        when(veranstaltung.getVeranstaltungSportjahr()).thenReturn(2024L);
        when(veranstaltungDAO.findById(6L)).thenReturn(veranstaltung);
        Long result = nameMappingComponent.getSportjahrForVeranstaltungsId(6L);
        assertEquals(2024L, result.longValue());
    }

    @Test
    public void testGetUserEmailForUserId() {
        var user = mock(de.bogenliga.application.business.user.impl.entity.UserBE.class);
        when(user.getUserEmail()).thenReturn("test@example.com");
        when(userDAO.findById(7L)).thenReturn(user);

        assertEquals("test@example.com", nameMappingComponent.getUserEmailForUserId(7L));
    }

    @Test
    public void testGetVereinnameForVereinId() {
        VereinBEext verein = mock(VereinBEext.class);
        when(verein.getVereinName()).thenReturn("VereinZ");
        when(vereinDAOext.findById(8L)).thenReturn(verein);

        assertEquals("VereinZ", nameMappingComponent.getVereinnameForVereinId(8L));
    }

    @Test
    public void testGetVereinnameForDsbMitgliedId() {
        DsbMitgliedBE mitglied = mock(DsbMitgliedBE.class);
        VereinBEext verein = mock(VereinBEext.class);
        when(mitglied.getDsbMitgliedVereinsId()).thenReturn(9L);
        when(dsbMitgliedDAO.findById(10L)).thenReturn(mitglied);

        when(verein.getVereinName()).thenReturn("VereinA");
        when(vereinDAOext.findById(9L)).thenReturn(verein);

        assertEquals("VereinA", nameMappingComponent.getVereinnameForDsbMitgliedId(10L));
    }

    @Test
    public void testGetVeranstaltungsNameForDsbMannschaftId() {
        DsbMannschaftBEext mannschaft = mock(DsbMannschaftBEext.class);
        when(mannschaft.getVeranstaltungName()).thenReturn("EventX");
        when(dsbMannschaftDAOext.findVeranstaltungAndWettkampfById(11L)).thenReturn(Collections.singletonList(mannschaft));

        assertEquals("EventX", nameMappingComponent.getVeranstaltungsNameForDsbMannschaftId(11L));
        when(dsbMannschaftDAOext.findVeranstaltungAndWettkampfById(12L)).thenReturn(Collections.emptyList());
        assertNull(nameMappingComponent.getVeranstaltungsNameForDsbMannschaftId(12L));
    }

    @Test
    public void testGetVeranstaltungsNameForVeranstaltungsId() {
        var veranstaltung = mock(de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE.class);
        when(veranstaltung.getVeranstaltungName()).thenReturn("EventY");
        when(veranstaltungDAO.findById(13L)).thenReturn(veranstaltung);

        assertEquals("EventY", nameMappingComponent.getVeranstaltungsNameForVeranstaltungsId(13L));
    }

    @Test
    public void testGetWettkampftypNameForWettkampftypId() {
        var typ = mock(de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE.class);
        when(typ.getwettkampftypname()).thenReturn("Typ1");
        when(wettkampfTypDAO.findById(14L)).thenReturn(typ);

        assertEquals("Typ1", nameMappingComponent.getWettkampftypNameForWettkampftypId(14L));
    }
}