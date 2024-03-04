package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.SQLException;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSchuetzeMapper implements ValueObjectMapper {
    private final AltsystemUebersetzung altsystemUebersetzung;

    public AltsystemSchuetzeMapper(AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     * Konvertiert die Daten eines Schützen aus dem Altsystem in ein Objekt vom Typ DsbMitgliedDO im neuen System.
     *
     * @param dsbMitgliedDO Das Zielobjekt vom Typ DsbMitgliedDO, in das die Daten konvertiert werden sollen.
     * @param altsystemSchuetzeDO Das Objekt, das die Daten des Schützen im Altsystem enthält.
     * @return Ein Objekt vom Typ DsbMitgliedDO, das die konvertierten Daten des Schützen im neuen System enthält.
     * @throws SQLException Falls ein Fehler bei der Verarbeitung der Daten aus der Datenbank auftritt.
     */
    public DsbMitgliedDO toDO(DsbMitgliedDO dsbMitgliedDO, AltsystemSchuetzeDO altsystemSchuetzeDO) throws SQLException {
        // Namen des Schützen parsen, um Vor- und Nachnamen zu trennen
        String[] parsedName = parseName(altsystemSchuetzeDO);

        // Vereins-ID im neuen System abrufe
        Long vereinID = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemSchuetzeDO.getMannschaft_id()).getBogenligaId();

        // Vor- und Nachnamen sowie Vereins-ID des Schützen setzen
        dsbMitgliedDO.setVorname(parsedName[1]);
        dsbMitgliedDO.setNachname(parsedName[0]);
        dsbMitgliedDO.setVereinsId(vereinID);

        return dsbMitgliedDO;
    }

    /**
     * Fügt Standardfelder zu einem DsbMitgliedDO-Objekt hinzu, die nicht aus dem Altsystem übernommen werden können.
     *
     * @param dsbMitgliedDO Das Zielobjekt vom Typ DsbMitgliedDO, zu dem Standardfelder hinzugefügt werden sollen.
     * @param currentDsbMitglied Die ID des aktuellen DsbMitglieds.
     * @return Ein Objekt vom Typ DsbMitgliedDO mit den hinzugefügten Standardfeldern.
     */
    public DsbMitgliedDO addDefaultFields (DsbMitgliedDO dsbMitgliedDO, long currentDsbMitglied) {
        // Standardwerte die nicht aus dem altSystem übernommen werden können
        dsbMitgliedDO.setGeburtsdatum(null);
        dsbMitgliedDO.setNationalitaet("D");
        dsbMitgliedDO.setUserId(null); // dsb_mitglied_benutzer_id
        dsbMitgliedDO.setKampfrichter(false);

        return dsbMitgliedDO;
    }

    /**
     * Diese Funktion analysiert den Namen eines Schützen aus dem Altsystem und trennt ihn in Vorname und Nachname auf.
     * Der Name ist im Altsystem in einem einzelnen Attribut (name) gespeichert und möglicherweise unterschiedlich getrennt.
     * Das Parsing ist erforderlich, um den Namen entsprechend in Vorname und Nachname zu splitten, da im neuen System
     * separate Attribute (nachname, vorname) für diese Zwecke existieren.
     *
     * @param altsystemSchuetzeDO Der Schütze aus dem Altsystem, dessen Name geparsed werden soll.
     * @return Ein String-Array, das den Vor- und Nachnamen des Schützen enthält, wobei der Nachname an erster und der Vorname an zweiter Stelle steht.
     */
    public String[] parseName(AltsystemSchuetzeDO altsystemSchuetzeDO) {
        // Parse Name um Vor- und Nachname zu trennen
        String[] schuetzeName;

        // Fall 1: Name ist getrennt durch Komma + beliebig viele Leerzeichen
        if (altsystemSchuetzeDO.getName().contains(",")) {
            schuetzeName = altsystemSchuetzeDO.getName().split(",");
            schuetzeName[0] = schuetzeName[0].replaceAll("\\s+", "");
            schuetzeName[1] = schuetzeName[1].replaceAll("\\s+", "");
        }
        // Fall 2: Name ist getrennt durch >= 1 Leerzeichen
        else {
            schuetzeName = altsystemSchuetzeDO.getName().split("\\s+");
            schuetzeName[0] = schuetzeName[0].trim(); // Vorname ohne Leerzeichen am Anfang und Ende
            schuetzeName[1] = schuetzeName[1].trim(); // Nachname ohne Leerzeichen am Anfang und Ende
        }

        return schuetzeName;
    }

    /**
     * Diese Funktion generiert einen eindeutigen Identifier für einen Schützen im neuen System.
     * Der Identifier wird aus dem Vor- und Nachnamen des Schützen sowie der Vereins-ID zusammengesetzt.
     *
     * @param altsystemSchuetzeDO Der Schütze aus dem Altsystem, für den der Identifier generiert werden soll.
     * @return Ein String, der den eindeutigen Identifier für den Schützen im neuen System darstellt.
     */
    public String getIdentifier(AltsystemSchuetzeDO altsystemSchuetzeDO) {
        // Namen des Schützen parsen
        String[] parsedName = parseName(altsystemSchuetzeDO);

        // Vereins-ID im neuen System finden
        Long vereinId = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                altsystemSchuetzeDO.getMannschaft_id()).getBogenligaId();

        // Identifier "firstName"+"lastName"+"vereinId" aufbauen
        String dsbMitgliedIdentifier = parsedName[1]+parsedName[0]+vereinId;

        return dsbMitgliedIdentifier;
    }

    /**
     * Diese Funktion sucht anhand des DSB-Mitglieder-Identifiers nach einem entsprechenden Datensatz im Altsystem.
     *
     * @param dsbMitgliedIdentifier Der Identifier des DSB-Mitglieds, nach dem gesucht werden soll.
     * @return Ein Objekt vom Typ AltsystemUebersetzungDO, das den Datensatz im Altsystem repräsentiert, der dem gesuchten DSB-Mitglied entspricht.
     * @throws SQLException Falls ein Fehler bei der Abfrage des Datensatzes aus der Datenbank auftritt.
     */
    public AltsystemUebersetzungDO getSchuetzeByIdentifier(Long dsbMitgliedIdentifier) throws SQLException {
        // Suchen des entsprechenden Datensatzes im Altsystem anhand der Altsystem ID
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, dsbMitgliedIdentifier);

        return altsystemUebersetzungDO;
    }


}