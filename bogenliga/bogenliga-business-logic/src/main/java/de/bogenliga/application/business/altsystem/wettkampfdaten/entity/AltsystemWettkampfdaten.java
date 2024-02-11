package de.bogenliga.application.business.altsystem.wettkampfdaten.entity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
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

    /**
     Create match-Objects with given data of the legacy system.*
     @param altsystemWettkampfdatenDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void create(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, long currentUserId){
        // Daten sind im Altsystem redundant
        // Bearbeitung nur, falls Sec = 0 ist
        if (altsystemWettkampfdatenDO.getSec() == 0){
            List<WettkampfDO> wettkampfTage = altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemWettkampfdatenDO, currentUserId);

            MatchDO[] match = new MatchDO[2];
            match[0] = new MatchDO();
            match[1] = new MatchDO();
            match = altsystemMatchMapper.toDO(match, altsystemWettkampfdatenDO);
            match = altsystemMatchMapper.addDefaultFields(match, wettkampfTage);

            match[0] = matchComponent.create(match[0], currentUserId);
            match[1] = matchComponent.create(match[1], currentUserId);

            saveAnzahlSaetze(altsystemWettkampfdatenDO, match);

            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId(), match[0].getId(), String.valueOf(match[1].getId()));
        }

    }

    /**
     Update existing match-Objects with given data of the legacy system.*
     @param altsystemWettkampfdatenDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void update(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, long currentUserId){
        // Daten sind im Altsystem redundant
        // Bearbeitung nur, falls Sec = 0 ist
        if (altsystemWettkampfdatenDO.getSec() == 0){
            // Zugehörige Matches aus Übersetzungstabelle holen
            AltsystemUebersetzungDO wettkampfdatenUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId());
            MatchDO[] match = new MatchDO[2];
            // Erstgenannte Mannschafts-ID ist im Feld "BogenligaId" gespeichert, zweitgenannte Mannschafts-ID im Feld Wert
            match[0] = matchComponent.findById(wettkampfdatenUebersetzung.getBogenligaId());
            match[1] = matchComponent.findById(Long.parseLong(wettkampfdatenUebersetzung.getWert()));
            // Mapping des Datensatzes
            match = altsystemMatchMapper.toDO(match, altsystemWettkampfdatenDO);

            // Matches updaten
            matchComponent.update(match[0], currentUserId);
            matchComponent.update(match[1], currentUserId);

            // Anzahl Sätze in Übersetzungstabelle updaten
            saveAnzahlSaetze(altsystemWettkampfdatenDO, match);
        }
    }


    /**
     Writes the number of played sets in a match into the translation table*
     @param altsystemWettkampfdatenDO data of the legacy system
     @param matchDO created match objects
     */
    public void saveAnzahlSaetze(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, MatchDO[] matchDO){
        int anzahlSaetze = 5;
        if (altsystemWettkampfdatenDO.getSatz4() == 0){
            anzahlSaetze = 3;
        } else if (altsystemWettkampfdatenDO.getSatz5() == 0){
            anzahlSaetze = 4;
        }
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, matchDO[0].getId(), 0L, String.valueOf(anzahlSaetze));
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, matchDO[1].getId(), 0L, String.valueOf(anzahlSaetze));
    }

}
