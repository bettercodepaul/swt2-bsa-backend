package de.bogenliga.application.business.veranstaltung.impl.entity;

import java.sql.Date;
import org.junit.Test;
import org.mockito.InjectMocks;
import de.bogenliga.application.business.liga.impl.entity.LigaBEext;
import junit.framework.TestCase;
import static org.assertj.core.api.Assertions.assertThat;

public class VeranstaltungBEextTest {


    @Test
    public void assertConstructor_initializesAllFields() {
        // Arrange
        final Long VERANSTALTUNG_ID = 1L;
        final Long VERANSTALTUNG_LIGA_ID = 2L;
        final Long VERANSTALTUNG_WETTKAMPFTYP_ID = 3L;
        final String VERANSTALTUNG_NAME = "Test Veranstaltung";
        final Long VERANSTALTUNG_SPORTJAHR = 2023L;
        final Date VERANSTALTUNG_MELDEDEADLINE = new Date(2L);
        final Long VERANSTALTUNG_LIGALEITER_ID = 4L;
        final Integer VERANSTALTUNG_PHASE = 1;
        final Integer VERANSTALTUNG_GROESSE = 10;
        final String LIGA_LEITER_EMAIL = "test@ligaleiter.com";
        final String LIGA_NAME = "Test Liga";
        final String WETTKAMPFTYP_NAME = "Test Wettkampftyp";

        // Act
        VeranstaltungBEext underTest = new VeranstaltungBEext(
                VERANSTALTUNG_ID, VERANSTALTUNG_LIGA_ID, VERANSTALTUNG_WETTKAMPFTYP_ID, VERANSTALTUNG_NAME,
                VERANSTALTUNG_SPORTJAHR, VERANSTALTUNG_MELDEDEADLINE, VERANSTALTUNG_LIGALEITER_ID,
                VERANSTALTUNG_PHASE, VERANSTALTUNG_GROESSE, LIGA_LEITER_EMAIL, LIGA_NAME, WETTKAMPFTYP_NAME
        );
        underTest.setVeranstaltungId(VERANSTALTUNG_ID);
        underTest.setVeranstaltungLigaId(VERANSTALTUNG_LIGA_ID);
        underTest.setVeranstaltungWettkampftypId(VERANSTALTUNG_WETTKAMPFTYP_ID);
        underTest.setVeranstaltungName(VERANSTALTUNG_NAME);
        underTest.setVeranstaltungSportjahr(VERANSTALTUNG_SPORTJAHR);
        underTest.setVeranstaltungMeldedeadline(VERANSTALTUNG_MELDEDEADLINE);
        underTest.setVeranstaltungLigaleiterId(VERANSTALTUNG_LIGALEITER_ID);
        underTest.setVeranstaltungPhase(VERANSTALTUNG_PHASE);
        underTest.setVeranstaltungGroesse(VERANSTALTUNG_GROESSE);
        underTest.setLigaLeiterEmail(LIGA_LEITER_EMAIL);
        underTest.setLigaName(LIGA_NAME);
        underTest.setwettkampftypName(WETTKAMPFTYP_NAME);
        // Assert
        assertThat(underTest.getVeranstaltungId()).isEqualTo(VERANSTALTUNG_ID);
        assertThat(underTest.getVeranstaltungLigaId()).isEqualTo(VERANSTALTUNG_LIGA_ID);
        assertThat(underTest.getVeranstaltungWettkampftypId()).isEqualTo(VERANSTALTUNG_WETTKAMPFTYP_ID);
        assertThat(underTest.getVeranstaltungName()).isEqualTo(VERANSTALTUNG_NAME);
        assertThat(underTest.getVeranstaltungSportjahr()).isEqualTo(VERANSTALTUNG_SPORTJAHR);
        assertThat(underTest.getVeranstaltungMeldedeadline()).isEqualTo(VERANSTALTUNG_MELDEDEADLINE);
        assertThat(underTest.getVeranstaltungLigaleiterId()).isEqualTo(VERANSTALTUNG_LIGALEITER_ID);
        assertThat(underTest.getVeranstaltungPhase()).isEqualTo(VERANSTALTUNG_PHASE);
        assertThat(underTest.getVeranstaltungGroesse()).isEqualTo(VERANSTALTUNG_GROESSE);
        assertThat(underTest.getLigaLeiterEmail()).isEqualTo(LIGA_LEITER_EMAIL);
        assertThat(underTest.getLigaName()).isEqualTo(LIGA_NAME);
        assertThat(underTest.getwettkampftypname()).isEqualTo(WETTKAMPFTYP_NAME);
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() {
        // Arrange
        final Long VERANSTALTUNG_ID = 1L;
        final Long VERANSTALTUNG_LIGA_ID = 2L;
        final Long VERANSTALTUNG_WETTKAMPFTYP_ID = 3L;
        final String VERANSTALTUNG_NAME = "Test Veranstaltung";
        final Long VERANSTALTUNG_SPORTJAHR = 2023L;
        final Date VERANSTALTUNG_MELDEDEADLINE = new Date(2L);
        final Long VERANSTALTUNG_LIGALEITER_ID = 4L;
        final Integer VERANSTALTUNG_PHASE = 1;
        final Integer VERANSTALTUNG_GROESSE = 10;
        final String LIGA_LEITER_EMAIL = "test@ligaleiter.com";
        final String LIGA_NAME = "Test Liga";
        final String WETTKAMPFTYP_NAME = "Test Wettkampftyp";

        VeranstaltungBEext underTest = new VeranstaltungBEext(
                VERANSTALTUNG_ID, VERANSTALTUNG_LIGA_ID, VERANSTALTUNG_WETTKAMPFTYP_ID, VERANSTALTUNG_NAME,
                VERANSTALTUNG_SPORTJAHR, VERANSTALTUNG_MELDEDEADLINE, VERANSTALTUNG_LIGALEITER_ID,
                VERANSTALTUNG_PHASE, VERANSTALTUNG_GROESSE, LIGA_LEITER_EMAIL, LIGA_NAME, WETTKAMPFTYP_NAME
        );

        // Act
        String result = underTest.toString();

        // Assert
        String expected = "VeranstaltungBEext{" +
                "ligaName=Test Liga" +
                ", ligaLeiterEmail='test@ligaleiter.com" +
                ", wettkampftypName='Test Wettkampftyp" +
                "}";
        assertThat(result).isEqualTo(expected);
    }
}
