package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDAO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;
import de.bogenliga.application.common.component.dao.BasicDAO;


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

        // Objekt anhand des identifier zurückgeben
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemSchuetzeMapper.getDsbMitgliedDO(parsedIdentifier);

        // wenn es noch keinen identifier in der UbersetzungsTabelle gibt
        if (altsystemUebersetzungDO == null) {
            // dann schreib Daten in diesen schuetzen rein
            dsbMitgliedDO = altsystemSchuetzeMapper.toDO(dsbMitgliedDO, altsystemSchuetzeDO);

            // add data to Uebersetzungstabelle
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schütze_Verein, altsystemSchuetzeDO.getId(),
                    altsystemUebersetzungDO.getBogenligaId(), altsystemUebersetzungDO.getWert());

            // Add data to Dsb_Mitglied table
            dsbMitgliedDO = dsbMitgliedComponent.create(dsbMitgliedDO, currentUserId);

        }
        // wenn diesen identifer bereits existiert
        else {
            // Schreib mannschaftsnamen in Schütze_Mannschaft
            String mannschaftName = String.valueOf(altsystemUebersetzung.findByAltsystemID(
                    AltsystemUebersetzungKategorie.Schütze_Mannschaft, altsystemSchuetzeDO.getMannschaft_id()));

            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schütze_Mannschaft, altsystemSchuetzeDO.getMannschaft_id(),
                    altsystemSchuetzeDO.getMannschaft_id(), mannschaftName);

        }
    }

    @Override
    public void update(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId){
    }
}