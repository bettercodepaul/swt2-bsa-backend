package de.bogenliga.application.business.altsystem.saison.entity;


import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * Test for AltsystemSaison
 *
 * @author Paul Roscher     paul.roscher@student.reutlingen-university.de
 */
public class AltsystemSaisonTest {

    // Constants for Mock Data
    private static final long CURRENTUSERID = 1L;
    private static final long SPORTJAHR = 2022L;
    private static final String SPORTJAHR_AS_STRING = "2022";
    private static final String SAISON_NAME = "2021 - 2022";
    private static final long SAISON_ID = 1L;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;

    @InjectMocks
    private AltsystemSaison altsystemSaison;

    // test
    @Test
    public void testCreate() {
        // prepare test data
        AltsystemSaisonDO altsystemSaisonDOMock = new AltsystemSaisonDO();
        altsystemSaisonDOMock.setName(SAISON_NAME);

        // call test method
        altsystemSaison.create(altsystemSaisonDOMock, CURRENTUSERID);

        // assert result
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(
                AltsystemUebersetzungKategorie.Saison_Sportjahr,
                altsystemSaisonDOMock.getId(),
                0L,
                SPORTJAHR_AS_STRING
        );
    }

    @Test
    public void testUpdate_TranslationFound_SportjahrDifferent() {
        // prepare test data
        AltsystemSaisonDO altsystemSaisonDOMock = new AltsystemSaisonDO();
        altsystemSaisonDOMock.setId(SAISON_ID);
        altsystemSaisonDOMock.setName(SAISON_NAME);

        // Mock behavior
        AltsystemUebersetzungDO saisonUebersetzung = new AltsystemUebersetzungDO();
        saisonUebersetzung.setWert("2020");
        when(altsystemUebersetzung.findByAltsystemID(any(), anyLong())).thenReturn(saisonUebersetzung);

        // call test method
        altsystemSaison.update(altsystemSaisonDOMock, CURRENTUSERID);

        // assert result
        verify(altsystemUebersetzung).updateOrInsertUebersetzung(
                AltsystemUebersetzungKategorie.Saison_Sportjahr,
                altsystemSaisonDOMock.getId(),
                0L,
                SPORTJAHR_AS_STRING
        );

    }
    @Test
    public void testUpdate_TranslationFound_SportjahrNotDifferent() {
        // prepare test data
        AltsystemSaisonDO altsystemSaisonDOMock = new AltsystemSaisonDO();
        altsystemSaisonDOMock.setId(SAISON_ID);
        altsystemSaisonDOMock.setName(SAISON_NAME);

        // Mock behavior
        AltsystemUebersetzungDO saisonUebersetzung = new AltsystemUebersetzungDO();
        saisonUebersetzung.setWert(SPORTJAHR_AS_STRING);
        when(altsystemUebersetzung.findByAltsystemID(any(), anyLong())).thenReturn(saisonUebersetzung);

        // call test method
        altsystemSaison.update(altsystemSaisonDOMock, CURRENTUSERID);

        // assert result
        verify(altsystemUebersetzung, never()).updateOrInsertUebersetzung(
                any(),
                anyLong(),
                anyLong(),
                anyString()
        );

    }

    @Test
    public void testUpdate_TranslationNotFound() {
        // prepare test data
        AltsystemSaisonDO altsystemSaisonDOMock = new AltsystemSaisonDO();
        altsystemSaisonDOMock.setId(SAISON_ID);

        // Mock behavior
        when(altsystemUebersetzung.findByAltsystemID(any(), anyLong())).thenReturn(null);

        // call test method
        try {
            altsystemSaison.update(altsystemSaisonDOMock, CURRENTUSERID);
            fail("Expected BusinessException was not thrown");
        } catch (BusinessException e) {
            // Successfully caught the expected exception
            assertEquals(ErrorCode.ENTITY_NOT_FOUND_ERROR, e.getErrorCode());
            assertEquals("ENTITY_NOT_FOUND_ERROR: No result found for ID '1'", e.getMessage());
        }

    }

    @Test
    public void testGetSportjahr() {

        // Act
        String result = altsystemSaison.getSportjahr(SAISON_NAME);

        // Assert
        assertEquals(SPORTJAHR, Long.parseLong(result));
    }
}
