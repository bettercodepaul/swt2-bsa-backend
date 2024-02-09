package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSchuetze implements AltsystemEntity<AltsystemSchuetzeDO> {

    private final AltsystemSchuetzeMapper altsystemSchuetzeMapper;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;


    @Autowired
    public AltsystemSchuetze(final AltsystemSchuetzeMapper altsystemSchuetzeMapper, final DsbMitgliedComponent dsbMitgliedComponent,
                             AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemSchuetzeMapper = altsystemSchuetzeMapper;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    @Override
    public void create(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId) throws SQLException {
        // leeres Objekt erstellen
        DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO();

        // identifier speichern
        String parsedIdentifier = altsystemSchuetzeMapper.getIdentifier(altsystemSchuetzeDO);

        AltsystemUebersetzungDO schuetzeUebersetzung = altsystemSchuetzeMapper.getDsbMitgliedDO(parsedIdentifier);
        Long dsbMitgliedId = null;

        // wenn es noch keinen identifier in der UbersetzungsTabelle gibt
        if (schuetzeUebersetzung == null) {

            // dann schreib Daten in diesen schuetzen rein
            dsbMitgliedDO = altsystemSchuetzeMapper.toDO(dsbMitgliedDO, altsystemSchuetzeDO);

            // Add data to Dsb_Mitglied table (Create ID)
            dsbMitgliedDO = dsbMitgliedComponent.create(dsbMitgliedDO, currentUserId);

            dsbMitgliedId = dsbMitgliedDO.getId();

        }
        // wenn diesen identifer bereits existiert
        else {

            dsbMitgliedId = schuetzeUebersetzung.getBogenligaId();

        }

        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, altsystemSchuetzeDO.getId(),
                dsbMitgliedId, parsedIdentifier);

        // Objekt anhand des identifier zurückgeben (für Ergebniss Tabelle)
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft,
                altsystemSchuetzeDO.getMannschaft_id());

        // add data to Uebersetzungstabelle (für Ergebniss Tabelle)
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_Mannschaft, altsystemSchuetzeDO.getId(),
                altsystemUebersetzungDO.getBogenligaId(), "");

    }

    @Override
    public void update(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId){
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,
                altsystemSchuetzeDO.getId());

        DsbMitgliedDO dsbMitgliedDO = dsbMitgliedComponent.findById(altsystemUebersetzungDO.getBogenligaId());
        String namen [] = altsystemSchuetzeMapper.parseName(altsystemSchuetzeDO);
        dsbMitgliedDO.setVorname(altsystemSchuetzeDO.getName());
        dsbMitgliedComponent.update(dsbMitgliedDO, currentUserId);

    }
}