package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import javax.validation.constraints.Null;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDAO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
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
    private AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    private DsbMitgliedComponent dsbMitgliedComponent;

    public DsbMitgliedDO toDO(DsbMitgliedDO dsbMitgliedDO, AltsystemSchuetzeDO altsystemSchuetzeDO) throws SQLException {
        String[] parsedName = parseName(altsystemSchuetzeDO);

        // check weather identifier exist in UebersetzungsTabelle
        if (altsystemUebersetzungDAO.findByWert(AltsystemUebersetzungKategorie.Schütze_Verein, getIdentifier(altsystemSchuetzeDO)) == null) {
            // if not exist set attributes
            dsbMitgliedDO.setVorname(parsedName[1]);
            dsbMitgliedDO.setNachname(parsedName[0]);
            dsbMitgliedDO.setVereinsId(altsystemUebersetzungDAO.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                    altsystemSchuetzeDO.getMannschaft_id()).getBogenligaId());

            //untere Werte können hier nicht gesetzt werden, da sie im Altsystem in der Entität Schuetze nicht existieren
//            dsbMitgliedDO.setGeburtsdatum(null);
//            dsbMitgliedDO.setNationalitaet(null);
//            dsbMitgliedDO.setMitgliedsnummer(null);
        }
        return dsbMitgliedDO;
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

    public String getIdentifier(AltsystemSchuetzeDO altsystemSchuetzeDO) {

        String[] parsedName = parseName(altsystemSchuetzeDO);

        Long vereinId = altsystemUebersetzungDAO.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Verein,
                altsystemSchuetzeDO.getMannschaft_id()).getBogenligaId();

        // build Identifier "firstName"+"lastName"+"vereinId"
        String dsbMitgliedIdentifier = parsedName[0]+parsedName[1]+vereinId;

        return dsbMitgliedIdentifier;
    }

    public AltsystemUebersetzungDO getDsbMitgliedDO(String dsbMitgliedIdentifier) throws SQLException {

        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzungDAO.findByWert(AltsystemUebersetzungKategorie.Schütze_Verein, dsbMitgliedIdentifier);

        return altsystemUebersetzungDO;
    }


}