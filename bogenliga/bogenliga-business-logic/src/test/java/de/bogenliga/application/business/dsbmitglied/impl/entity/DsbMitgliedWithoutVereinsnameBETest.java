package de.bogenliga.application.business.dsbmitglied.impl.entity;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

public class DsbMitgliedWithoutVereinsnameBETest {

    private DsbMitgliedWithoutVereinsnameBE mitglied;
    private Long id;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private String nationalitaet;
    private String mitgliedsnummer;
    private Long vereinsId;
    private Long userId;
    private Date beitrittsdatum;

    @Before
    public void setUp() {
        id = 1L;
        vorname = "Max";
        nachname = "Mustermann";
        geburtsdatum = Date.valueOf("2000-01-01");
        nationalitaet = "DE";
        mitgliedsnummer = "123456";
        vereinsId = 100L;
        userId = 200L;
        beitrittsdatum = Date.valueOf("2023-01-01");
        mitglied = new DsbMitgliedWithoutVereinsnameBE(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId, beitrittsdatum);
    }

    @Test
    public void testConstructor() {
        assertEquals(id, mitglied.getDsbMitgliedId());
        assertEquals(vorname, mitglied.getDsbMitgliedVorname());
        assertEquals(nachname, mitglied.getDsbMitgliedNachname());
        assertEquals(geburtsdatum, mitglied.getDsbMitgliedGeburtsdatum());
        assertEquals(nationalitaet, mitglied.getDsbMitgliedNationalitaet());
        assertEquals(mitgliedsnummer, mitglied.getDsbMitgliedMitgliedsnummer());
        assertEquals(vereinsId, mitglied.getDsbMitgliedVereinsId());
        assertEquals(userId, mitglied.getDsbMitgliedUserId());
        assertEquals(beitrittsdatum, mitglied.getDsbMitgliedBeitrittsdatum());
    }

    @Test
    public void testSettersAndGetters() {
        Long newId = 2L;
        String newVorname = "Anna";
        String newNachname = "Musterfrau";
        Date newGeburtsdatum = Date.valueOf("1990-01-01");
        String newNationalitaet = "FR";
        String newMitgliedsnummer = "654321";
        Long newVereinsId = 101L;
        Long newUserId = 201L;
        Date newBeitrittsdatum = Date.valueOf("2024-01-01");

        mitglied.setDsbMitgliedId(newId);
        mitglied.setDsbMitgliedVorname(newVorname);
        mitglied.setDsbMitgliedNachname(newNachname);
        mitglied.setDsbMitgliedGeburtsdatum(newGeburtsdatum);
        mitglied.setDsbMitgliedNationalitaet(newNationalitaet);
        mitglied.setDsbMitgliedMitgliedsnummer(newMitgliedsnummer);
        mitglied.setDsbMitgliedVereinsId(newVereinsId);
        mitglied.setDsbMitgliedUserId(newUserId);
        mitglied.setDsbMitgliedBeitrittsdatum(newBeitrittsdatum);

        assertEquals(newId, mitglied.getDsbMitgliedId());
        assertEquals(newVorname, mitglied.getDsbMitgliedVorname());
        assertEquals(newNachname, mitglied.getDsbMitgliedNachname());
        assertEquals(newGeburtsdatum, mitglied.getDsbMitgliedGeburtsdatum());
        assertEquals(newNationalitaet, mitglied.getDsbMitgliedNationalitaet());
        assertEquals(newMitgliedsnummer, mitglied.getDsbMitgliedMitgliedsnummer());
        assertEquals(newVereinsId, mitglied.getDsbMitgliedVereinsId());
        assertEquals(newUserId, mitglied.getDsbMitgliedUserId());
        assertEquals(newBeitrittsdatum, mitglied.getDsbMitgliedBeitrittsdatum());
    }

    @Test
    public void testToString() {
        String expected = "DsbMitgliedWithoutVereinsNameBE{" +
                "dsbMitgliedId=" + id +
                ", dsbMitgliedVorname='" + vorname + '\'' +
                ", dsbMitgliedNachname='" + nachname + '\'' +
                ", dsbMitgliedGeburtsdatum='" + geburtsdatum + '\'' +
                ", dsbMitgliedNationalitaet='" + nationalitaet + '\'' +
                ", dsbMitgliedMitgliedsnummer='" + mitgliedsnummer + '\'' +
                ", dsbMitgliedVereinsId=" + vereinsId +
                ", dsbMitgliedUserId=" + userId +
                ", dsbMitgliedBeitrittsdatum=" + beitrittsdatum +
                '}';

        assertEquals(expected, mitglied.toString());
    }
}
