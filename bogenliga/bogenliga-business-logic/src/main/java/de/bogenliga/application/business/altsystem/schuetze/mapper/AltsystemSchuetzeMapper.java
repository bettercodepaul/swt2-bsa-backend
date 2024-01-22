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

    private Connection connection;
    private VereinComponent vereinComponent;

    private AltsystemUebersetzung altsystemUebersetzung;

    public DsbMitgliedDO toDO(DsbMitgliedDO dsbMitglied, AltsystemSchuetzeDO altsystemSchuetzeDO) throws SQLException {

        // Checken ob der Schütze bereits in der FlyWay Tabelle exestiert
        //aus schützen die namen auslesen und trennen name (nachname, vorname)

        String[] name = null;

        // (Allmendinger , Michael)
        //argument 0 = nachname, argument 1 = vorname
        if (altsystemSchuetzeDO.getName().contains(",")) {
            name = altsystemSchuetzeDO.getName().split(",");
            name[0] = name[0].replaceAll("\\s+", "");
            name[1] = name[1].replaceAll("\\s+", "");

        } else {
            name = altsystemSchuetzeDO.getName().split(" ");
            name[0] = name[0].replaceAll("\\s+", "");
            name[1] = name[1].replaceAll("\\s+", "");
        }

            dsbMitglied.setVorname(name[1]);
            dsbMitglied.setNachname(name[0]);
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
