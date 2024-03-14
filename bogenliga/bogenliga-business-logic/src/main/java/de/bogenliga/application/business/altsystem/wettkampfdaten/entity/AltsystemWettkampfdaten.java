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
            List<WettkampfDO> wettkampfTage = altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemWettkampfdatenDO, currentUserId);

            MatchDO matchDO = new MatchDO();
            matchDO = altsystemMatchMapper.toDO(matchDO, altsystemWettkampfdatenDO);
            matchDO = altsystemMatchMapper.addDefaultFields(matchDO, wettkampfTage);

            matchDO = matchComponent.create(matchDO, currentUserId);

            saveAnzahlSaetze(altsystemWettkampfdatenDO, matchDO);

            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId(), matchDO.getId(), null);

    }

    /**
     Update existing match-Objects with given data of the legacy system.*
     @param altsystemWettkampfdatenDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void update(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, long currentUserId){
        // Zugehörige Matches aus Übersetzungstabelle holen
            AltsystemUebersetzungDO wettkampfdatenUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Wettkampfergebnis_Match, altsystemWettkampfdatenDO.getId());
            MatchDO matchDO = new MatchDO();
            // Erstgenannte Mannschafts-ID ist im Feld "BogenligaId" gespeichert, zweitgenannte Mannschafts-ID im Feld Wert
            matchDO = matchComponent.findById(wettkampfdatenUebersetzung.getBogenligaId());
            // Mapping des Datensatzes
            matchDO = altsystemMatchMapper.toDO(matchDO, altsystemWettkampfdatenDO);

            // Matches updaten
            matchComponent.update(matchDO, currentUserId);

            // Anzahl Sätze in Übersetzungstabelle updaten
            saveAnzahlSaetze(altsystemWettkampfdatenDO, matchDO);
    }


    /**
     Writes the number of played sets in a match into the translation table*
     @param altsystemWettkampfdatenDO data of the legacy system
     @param matchDO created match objects
     */
    public void saveAnzahlSaetze(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, MatchDO matchDO){
        int anzahlSaetze = 5;
        if (altsystemWettkampfdatenDO.getSatz4() == 0){
            anzahlSaetze = 3;
        } else if (altsystemWettkampfdatenDO.getSatz5() == 0){
            anzahlSaetze = 4;
        }
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Match_Saetze, matchDO.getId(), 0L, String.valueOf(anzahlSaetze));
    }

}
