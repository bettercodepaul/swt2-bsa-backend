package de.bogenliga.application.business.altsystem.wettkampfdaten.entity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemMatchMapper;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemWettkampftagMapper;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * Component to handle the import of a "Wettkampfdaten" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemWettkampfdaten implements AltsystemEntity<AltsystemWettkampfdatenDO> {

    private final AltsystemMatchMapper altsystemMatchMapper;
    private final AltsystemWettkampftagMapper altsystemWettkampftagMapper;
    private final MatchComponent matchComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemWettkampfdaten(AltsystemWettkampftagMapper altsystemWettkampftagMapper, final AltsystemMatchMapper altsystemMatchMapper, final MatchComponent matchComponent,
                                   AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemWettkampftagMapper = altsystemWettkampftagMapper;
        this.altsystemMatchMapper = altsystemMatchMapper;
        this.matchComponent = matchComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    @Override
    public void create(AltsystemWettkampfdatenDO altsystemDataObject, long currentUserId){
        // Daten sind im Altsystem redundant
        // Bearbeitung nur, falls Sec = 0 ist
        if (altsystemDataObject.getSec() == 0){
            List<WettkampfDO> wettkampfTage = altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemDataObject, currentUserId);

            MatchDO[] match = new MatchDO[2];
            match[0] = new MatchDO();
            match[1] = new MatchDO();
            match = altsystemMatchMapper.toDO(match, altsystemDataObject);
            match = altsystemMatchMapper.addDefaultFields(match, wettkampfTage);

            matchComponent.create(match[0], currentUserId);
            matchComponent.create(match[1], currentUserId);

            saveAnzahlSaetze(altsystemDataObject, match);
        }

    }

    @Override
    public void update(AltsystemWettkampfdatenDO altsystemDataObject, long currentUserId){
        // Get primary key from translation table

        // Find data in table with corresponding id
        MatchDO matchDO = matchComponent.findById(1L);

        // Map data to new object, don't add default fields
        // altsystemWettkampfdatenMapper.toDO(match, altsystemDataObject);

        // Update data in table with given primary key
        // ligaComponent.update(ligaDO, <UserID>);
    }


    public void saveAnzahlSaetze(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, MatchDO[] matchDOS){
        int anzahlSaetze = 5;
        if (altsystemWettkampfdatenDO.getSatz4() == 0){
            anzahlSaetze = 3;
        } else if (altsystemWettkampfdatenDO.getSatz5() == 0){
            anzahlSaetze = 4;
        }
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, matchDOS[0].getId(), 0L, String.valueOf(anzahlSaetze));
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, matchDOS[1].getId(), 0L, String.valueOf(anzahlSaetze));
    }

}
