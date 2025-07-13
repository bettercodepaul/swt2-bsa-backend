package de.bogenliga.application.business.schusszettel.api.types.inside;

import org.junit.Test;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class WettkampfInfoDOTest {

    @Test
    public void coverAllLines() {
        // Cover default constructor (lines 30-32)
        WettkampfInfoDO wettkampf1 = new WettkampfInfoDO();
        
        // Cover parameterized constructor (lines 34-52)
        Date testDate = new Date(System.currentTimeMillis());
        WettkampfInfoDO wettkampf2 = new WettkampfInfoDO(1L, 2L, testDate, "10:00", "Sporthalle", "Halle A", 
                                                         "Musterstraße 1", "12345", 3L, "Testveranstaltung", 
                                                         2023L, "Bundesliga", "Einzelwettkampf");
        
        // Cover all setters (lines 56, 59, 62, 65, 68, 71, 74, 77, 80, 83, 86, 89, 92)
        wettkampf1.setWettkampfId(4L);
        wettkampf1.setWettkampfTag(5L);
        wettkampf1.setWettkampfDatum(testDate);
        wettkampf1.setWettkampfBeginn("14:00");
        wettkampf1.setWettkampfOrtsname("Testort");
        wettkampf1.setWettkampfOrtsinfo("Info");
        wettkampf1.setWettkampfStrasse("Teststraße 2");
        wettkampf1.setWettkampfPlz("54321");
        wettkampf1.setVeranstaltungId(6L);
        wettkampf1.setVeranstaltungName("Test Event");
        wettkampf1.setVeranstaltungSportjahr(2024L);
        wettkampf1.setLigaName("Oberliga");
        wettkampf1.setWettkampftypName("Mannschaftswettkampf");
        
        // Cover all getters (lines 55, 58, 61, 64, 67, 70, 73, 76, 79, 82, 85, 88, 91)
        Long id1 = wettkampf1.getWettkampfId();
        Long tag1 = wettkampf1.getWettkampfTag();
        Date datum1 = wettkampf1.getWettkampfDatum();
        String beginn1 = wettkampf1.getWettkampfBeginn();
        String ortsname1 = wettkampf1.getWettkampfOrtsname();
        String ortsinfo1 = wettkampf1.getWettkampfOrtsinfo();
        String strasse1 = wettkampf1.getWettkampfStrasse();
        String plz1 = wettkampf1.getWettkampfPlz();
        Long veranstId1 = wettkampf1.getVeranstaltungId();
        String veranstName1 = wettkampf1.getVeranstaltungName();
        Long sportjahr1 = wettkampf1.getVeranstaltungSportjahr();
        String liga1 = wettkampf1.getLigaName();
        String typ1 = wettkampf1.getWettkampftypName();
        
        Long id2 = wettkampf2.getWettkampfId();
        Long tag2 = wettkampf2.getWettkampfTag();
        Date datum2 = wettkampf2.getWettkampfDatum();
        String beginn2 = wettkampf2.getWettkampfBeginn();
        String ortsname2 = wettkampf2.getWettkampfOrtsname();
        String ortsinfo2 = wettkampf2.getWettkampfOrtsinfo();
        String strasse2 = wettkampf2.getWettkampfStrasse();
        String plz2 = wettkampf2.getWettkampfPlz();
        Long veranstId2 = wettkampf2.getVeranstaltungId();
        String veranstName2 = wettkampf2.getVeranstaltungName();
        Long sportjahr2 = wettkampf2.getVeranstaltungSportjahr();
        String liga2 = wettkampf2.getLigaName();
        String typ2 = wettkampf2.getWettkampftypName();
        
        // Basic assertions
        assertThat(wettkampf1).isNotNull();
        assertThat(wettkampf2).isNotNull();
        assertThat(id1).isEqualTo(4L);
        assertThat(id2).isEqualTo(1L);
        assertThat(beginn2).isEqualTo("10:00");
        assertThat(ortsname1).isEqualTo("Testort");
        assertThat(veranstName2).isEqualTo("Testveranstaltung");
        assertThat(sportjahr2).isEqualTo(2023L);
    }
}