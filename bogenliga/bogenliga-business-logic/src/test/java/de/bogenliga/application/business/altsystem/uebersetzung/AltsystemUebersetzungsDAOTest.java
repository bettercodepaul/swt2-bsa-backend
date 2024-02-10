package de.bogenliga.application.business.altsystem.uebersetzung;

import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemUebersetzungsDAOTest {
    private final long ALTSYSTEM_ID = 1L;
    private final long BOGENLIGA_ID = 2L;
    private final String WERT = "Wert";
    private static final String UEBERSETZUNG_KATEGORIE = "Mannschaft_Veranstlatung";
    @Mock
    private BasicDAO basicDAO;
    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @InjectMocks
    private AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    @Before
    public void setUP() {
        basicDAO = mock(BasicDAO.class);
        altsystemUebersetzungDAO = new AltsystemUebersetzungDAO(basicDAO);

    }
    @Test
    public void testUpdateOrInsertUebersetzungInsertCase(){
        AltsystemUebersetzungKategorie kategorie = AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung;
        AltsystemUebersetzungDO altsystemUebersetzungDOMock = mock(AltsystemUebersetzungDO.class);

        when(altsystemUebersetzung.findByAltsystemID(any(), anyLong())).thenReturn(null);
        when(altsystemUebersetzungDOMock.getKategorie()).thenReturn("Mannschaft_Veranstaltung");
        when(altsystemUebersetzungDOMock.getAltsystemId()).thenReturn(ALTSYSTEM_ID);
        when(basicDAO.insertEntity(any(), any())).thenReturn(altsystemUebersetzungDOMock);

        altsystemUebersetzungDAO.updateOrInsertUebersetzung(kategorie, ALTSYSTEM_ID, BOGENLIGA_ID, WERT);

        verify(basicDAO, times(1)).insertEntity(any(), any());
        verify(basicDAO, never()).updateEntity(any(), any(), any());
    }

    @Test
    public void testUdateOrInsertUebersetzungUpdateCase() {
        AltsystemUebersetzungKategorie altsystemUebersetzungKategorie = AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung;
        BusinessEntityConfiguration<AltsystemUebersetzungDO> UebersetzungMock = mock(BusinessEntityConfiguration.class);
        AltsystemUebersetzungDO altsystemUebersetzungDO = mock(AltsystemUebersetzungDO.class);
        altsystemUebersetzungDO.setAltsystemId(ALTSYSTEM_ID);
        altsystemUebersetzungDO.setWert(WERT);
        altsystemUebersetzungDO.setBogenligaId(BOGENLIGA_ID);
        altsystemUebersetzungDO.setKategorie(UEBERSETZUNG_KATEGORIE);

        when(altsystemUebersetzungDAO.findByAltsystemID(altsystemUebersetzungKategorie, ALTSYSTEM_ID)).thenReturn(altsystemUebersetzungDO);
        when(basicDAO.updateEntity(UebersetzungMock, altsystemUebersetzungDO, "altsystemID")).thenAnswer(invocation -> invocation.getArgument(1));

        altsystemUebersetzungDAO.updateOrInsertUebersetzung(altsystemUebersetzungKategorie, ALTSYSTEM_ID, BOGENLIGA_ID, WERT);

        verify(basicDAO, never()).insertEntity(any(),any());
        verify(basicDAO, times(1)).updateEntity(any(), any(), any());
    }
    @Test
    public void testFindByAltsystemID() {
        AltsystemUebersetzungKategorie altsystemUebersetzungKategorie = AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung;
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(ALTSYSTEM_ID);
        altsystemUebersetzungDO.setBogenligaId(BOGENLIGA_ID);
        altsystemUebersetzungDO.setKategorie(UEBERSETZUNG_KATEGORIE);
        altsystemUebersetzungDO.setWert(WERT);

        when(basicDAO.selectSingleEntity(any(), any(), any(), any())).thenReturn(altsystemUebersetzungDO);

        AltsystemUebersetzungDO result = altsystemUebersetzungDAO.findByAltsystemID(altsystemUebersetzungKategorie, ALTSYSTEM_ID);

        verify(basicDAO).selectSingleEntity(any(),any(),any(),any());

        assertEquals(altsystemUebersetzungDO, result);
    }

    @Test
    public void testFindByWert() {
        AltsystemUebersetzungKategorie altsystemUebersetzungKategorie = AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung;
        AltsystemUebersetzungDO altsystemUebersetzungDO = new AltsystemUebersetzungDO();
        altsystemUebersetzungDO.setAltsystemId(ALTSYSTEM_ID);
        altsystemUebersetzungDO.setBogenligaId(BOGENLIGA_ID);
        altsystemUebersetzungDO.setKategorie(UEBERSETZUNG_KATEGORIE);
        altsystemUebersetzungDO.setWert(WERT);

        when(basicDAO.selectSingleEntity(any(), any(), any(), any())).thenReturn(altsystemUebersetzungDO);

        AltsystemUebersetzungDO result = altsystemUebersetzungDAO.findByAltsystemID(altsystemUebersetzungKategorie, ALTSYSTEM_ID);

        verify(basicDAO).selectSingleEntity(any(),any(),any(),any());

        assertEquals(altsystemUebersetzungDO, result);
    }

}
