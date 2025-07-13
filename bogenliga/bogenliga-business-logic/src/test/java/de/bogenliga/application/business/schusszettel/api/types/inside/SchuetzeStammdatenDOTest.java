package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzeStammdatenDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 16-18)
        SchuetzeStammdatenDO schuetze1 = new SchuetzeStammdatenDO();
        
        // Cover parameterized constructor (lines 20-25)
        SchuetzeStammdatenDO schuetze2 = new SchuetzeStammdatenDO(1L, 123, "Max", "Mustermann");
        
        // Cover all setters (lines 31-33, 39-41, 47-49, 55-57)
        schuetze1.setSchuetzenId(2L);
        schuetze1.setRueckennummer(456);
        schuetze1.setVorname("Anna");
        schuetze1.setNachname("Schmidt");
        
        // Cover all getters (lines 27-29, 35-37, 43-45, 51-53)
        Long id1 = schuetze1.getSchuetzenId();
        Integer ruecken1 = schuetze1.getRueckennummer();
        String vorname1 = schuetze1.getVorname();
        String nachname1 = schuetze1.getNachname();
        
        Long id2 = schuetze2.getSchuetzenId();
        Integer ruecken2 = schuetze2.getRueckennummer();
        String vorname2 = schuetze2.getVorname();
        String nachname2 = schuetze2.getNachname();
        
        // Basic assertions
        assertThat(schuetze1).isNotNull();
        assertThat(schuetze2).isNotNull();
        assertThat(id1).isEqualTo(2L);
        assertThat(id2).isEqualTo(1L);
        assertThat(vorname2).isEqualTo("Max");
        assertThat(nachname1).isEqualTo("Schmidt");
    }
}