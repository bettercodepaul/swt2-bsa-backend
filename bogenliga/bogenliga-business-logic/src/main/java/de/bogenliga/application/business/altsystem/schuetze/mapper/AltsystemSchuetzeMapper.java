package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
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


        String flywayValue = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_Verein, altsystemSchuetzeDO.getId()).getValue();
        Long flywayBogenligaID = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_Verein, altsystemSchuetzeDO.getId()).getBogenliga_id();

        //wenn altsystemName einem value in flyway und altsystemID einer bogenligaID in flyway entspricht -> Schuetze wurde bereits in neue Datenbank importiert
        if(altsystemID == flywayBogenligaID && altsystemName == flywayValue) {
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
            dsbMitglied.setVereinsId(altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein, (long) altsystemSchuetzeDO.getMannschaft_id()).getAltsystem_id());

            //unten aufgeführte Funktionen in DsbMitgliedDO nicht vorhanden - wie sollen diese Werte gesetzt werden?
            /*
            dsbMitglied.setCreated_at_utc(null);
            dsbMitglied.setCreated_by(0);
            dsbMitglied.setLast_modified_at_utc(null);
            dsbMitglied.setLast_modified_by(0);
            dsbMitglied.setVersion(0);
            */

        return dsbMitglied;
    }


    public DsbMitgliedDO addDefaultFields(DsbMitgliedDO dsbMitgliedDO) {
        // ID von Verein herausfinden

        vereinComponent.findById(dsbMitgliedDO.getId());
        
        
        return dsbMitgliedDO;
    }
}
