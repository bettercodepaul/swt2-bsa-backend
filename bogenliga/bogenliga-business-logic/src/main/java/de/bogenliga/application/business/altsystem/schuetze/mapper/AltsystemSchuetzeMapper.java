package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.SQLException;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSchuetzeMapper  implements ValueObjectMapper {
    private AltsystemUebersetzung altsystemUebersetzung;

    public DsbMitgliedDO toDO(AltsystemSchuetzeDO altsystemSchuetzeDO) throws SQLException {

        //dont execute method if argument is null
        if(altsystemSchuetzeDO == null){
            throw new NullPointerException(altsystemSchuetzeDO + " does not exist");
        }

        DsbMitgliedDO dsbMitglied = new DsbMitgliedDO();

        //prüfen ob Schütze mit Mannschafts-ID und Nachname + Vorname bereits in Flyway-Tabelle steht
        //nein? -> anlegen und in Tabelle schreiben
        //ja? -> return error
        String[] parsedName = parseName(altsystemSchuetzeDO);

        String altsystemName = parsedName[0] + ", " + parsedName[1]; // Allmendinger Michael
        Long altsystemID = altsystemSchuetzeDO.getId(); // MannschaftsID = 356


        String flywayValue = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_Verein, altsystemSchuetzeDO.getId()).getWert();
        Long flywayAltsystemID = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_Verein, altsystemSchuetzeDO.getId()).getAltsystemId();

        //wenn altsystemName einem value in flyway und altsystemID einer bogenligaID in flyway entspricht -> Schuetze wurde bereits in neue Datenbank importiert
        if(altsystemID == flywayAltsystemID && altsystemName.equals(flywayValue)) {
            //trigger-gruppe muss prüfen ob diese methode ein DsbMitgliedDO oder null zurückgibt und entsprechend agieren
            return null;
        }
        else {
            dsbMitglied.setVorname(parsedName[1]);
            dsbMitglied.setNachname(parsedName[0]);

            //untere Werte können hier nicht gesetzt werden, da sie im Altsystem in der Entität Schuetze nicht existieren
            dsbMitglied.setGeburtsdatum(null);
            dsbMitglied.setNationalitaet(null);
            dsbMitglied.setMitgliedsnummer(null);
            dsbMitglied.setVereinsId(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                            altsystemSchuetzeDO.getMannschaft_id()).getBogenligaId());


            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schütze_Verein,
                    altsystemID, dsbMitglied.getVereinsId(), altsystemName);
        }

        return dsbMitglied;
    }

    //Namen sind im Altsystem unterschiedlich getrennt, da als einzelnes Attribut (name) in der Tabelle gespeichert
    //parsing erfolgt um Namen in Vorname + Nachname zu splitten, da im neuen System zwei verschiedene Attribute (vorname, nachname) dafür existieren
    public String[] parseName(AltsystemSchuetzeDO altsystemSchuetzeDO){
        String[] schuetzeName;

        //Fall 1: Name ist getrennt durch Komma + beliebig viele Leerzeichen
        if (altsystemSchuetzeDO.getName().contains(",")) {
            schuetzeName = altsystemSchuetzeDO.getName().split(",");
            schuetzeName[0] = schuetzeName[0].replaceAll("\\s+", "");
            schuetzeName[1] = schuetzeName[1].replaceAll("\\s+", "");
        //Fall 2: Name ist getrennt durch >= 1 Leerzeichen
        } else {
            schuetzeName = altsystemSchuetzeDO.getName().split(" ");
            schuetzeName[0] = schuetzeName[0].replaceAll("\\s+", "");
            schuetzeName[1] = schuetzeName[1].replaceAll("\\s+", "");
        }

        return schuetzeName;
    }
}
