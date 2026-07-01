package de.bogenliga.application.services.v1.anzeigen;

import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenMatchDTO;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnzeigenMatchDTOTest {

    private final int matchNr = 1;
    private final String verein1 = "Test Verein";
    private final String verein2 = "Test e.V.";
    private final Integer[] schuesseVerein1 = new Integer[] { 5, 3, 4, 8, 2, 1 };
    private final Integer[] schuesseVerein2 = new Integer[] { 1, 2, 10, 8, 2, 3 };
    private final int totalVerein1 = 23;
    private final int totalVerein2 = 26;
    private final int satzpunkteVerein1 = 2;
    private final int satzpunkteVerein2 = 0;

    @Test
    public void noArgsConstructor_shouldInitializeWithDefaultValues() {
        // When
        AnzeigenMatchDTO result = new AnzeigenMatchDTO();

        // Then - Überprüfung der im Konstruktor fest hinterlegten Default-Werte
        assertThat(result.getMatchNr()).isZero();
        assertThat(result.getSatzpunkteVerein1()).isZero();
        assertThat(result.getSatzpunkteVerein2()).isZero();
        assertThat(result.getTotalVerein1()).isZero();
        assertThat(result.getTotalVerein2()).isZero();
        assertThat(result.getVerein1()).isEmpty();
        assertThat(result.getVerein2()).isEmpty();
        assertThat(result.getSchuesseVerein1()).isNull();
        assertThat(result.getSchuesseVerein2()).isNull();
    }

    @Test
    public void allArgsConstructor_shouldMapFieldsCorrectly() {
        // When - Konstruktor mit allen Parametern aufrufen
        AnzeigenMatchDTO result = new AnzeigenMatchDTO(matchNr, verein1, verein2, schuesseVerein1, schuesseVerein2, totalVerein1, totalVerein2, satzpunkteVerein1, satzpunkteVerein2);

        // Then
        assertThat(result.getMatchNr()).isEqualTo(matchNr);
        assertThat(result.getVerein1()).isEqualTo(verein1);
        assertThat(result.getVerein2()).isEqualTo(verein2);
        assertThat(result.getSchuesseVerein1()).isEqualTo(schuesseVerein1);
        assertThat(result.getSchuesseVerein2()).isEqualTo(schuesseVerein2);
        assertThat(result.getTotalVerein1()).isEqualTo(totalVerein1);
        assertThat(result.getTotalVerein2()).isEqualTo(totalVerein2);
        assertThat(result.getSatzpunkteVerein1()).isEqualTo(satzpunkteVerein1);
        assertThat(result.getSatzpunkteVerein2()).isEqualTo(satzpunkteVerein2);
    }

    @Test
    public void settersAndGetters_shouldWorkCorrectly() {
        // Given
        AnzeigenMatchDTO result = new AnzeigenMatchDTO();

        // When - Setter explizit aufrufen
        result.setMatchNr(matchNr);
        result.setVerein1(verein1);
        result.setVerein2(verein2);
        result.setSchuesseVerein1(schuesseVerein1);
        result.setSchuesseVerein2(schuesseVerein2);
        result.setTotalVerein1(totalVerein1);
        result.setTotalVerein2(totalVerein2);
        result.setSatzpunkteVerein1(satzpunkteVerein1);
        result.setSatzpunkteVerein2(satzpunkteVerein2);

        // Then - Getter validieren
        assertThat(result.getMatchNr()).isEqualTo(matchNr);
        assertThat(result.getVerein1()).isEqualTo(verein1);
        assertThat(result.getVerein2()).isEqualTo(verein2);
        assertThat(result.getSchuesseVerein1()).isEqualTo(schuesseVerein1);
        assertThat(result.getSchuesseVerein2()).isEqualTo(schuesseVerein2);
        assertThat(result.getTotalVerein1()).isEqualTo(totalVerein1);
        assertThat(result.getTotalVerein2()).isEqualTo(totalVerein2);
        assertThat(result.getSatzpunkteVerein1()).isEqualTo(satzpunkteVerein1);
        assertThat(result.getSatzpunkteVerein2()).isEqualTo(satzpunkteVerein2);
    }
}
