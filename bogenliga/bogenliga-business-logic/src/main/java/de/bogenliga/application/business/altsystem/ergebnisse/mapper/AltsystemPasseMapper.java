package de.bogenliga.application.business.altsystem.ergebnisse.mapper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Mappt ein Ergebnis aus dem Altsystem auf Passen im neuen System
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemPasseMapper {

    private final MatchComponent matchComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemPasseMapper(final MatchComponent matchComponent, final AltsystemUebersetzung altsystemUebersetzung){
        this.matchComponent = matchComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     Splits the points of an ergebnis-dataset to a 2-dimensional array which can be used to create the passe objects
     @param anzahlPunkte number of points scored by one member in one match
     @param anzahlSaetze number of sets played in a game
     @return 2 dimensional array with the points per arrow
     result[i][0] -> First arrow in a passe
     result[i][1] -> Second arrow in a passe
     */
    public int[][] getPassenpunkte (long anzahlPunkte, int anzahlSaetze) {

        int[][] punkte = new int[anzahlSaetze][2];

        // Aufteilung des Ergebnisses auf Ergebnis pro Pfeil
        // Eine Passe je Satz, zwei Pfeile je Passe
        int avgErgebnisProPfeil = (int) Math.floor(anzahlPunkte / (2.0 * anzahlSaetze));
        int restpunkte = (int) (anzahlPunkte % (2 * anzahlSaetze));

        // Aufteilung der Passenpunkte auf jew. 2 Pfeile
        for(int i = 0; i < anzahlSaetze; i++){
            for(int j = 0; j < 2; j++) {
                int ringzahl = avgErgebnisProPfeil;
                if (restpunkte > 0 && ringzahl < 10) {
                    ringzahl++;
                    restpunkte--;
                }
                punkte[i][j] = ringzahl;
            }
        }
        return punkte;
    }

    /**
     Creates a list of Passe objects which represent the Ergebnis dataset from the legacy system
     @param passen list in which the Passe objects should be stored
     @param altsystemDataObject ergebnis data
     @return list of Passe objects
     */
    public List<PasseDO> toDO(List<PasseDO> passen, AltsystemErgebnisseDO altsystemDataObject) {

        // Übersetzungstabelle schuetzeID --> DSBMitglied bzw. Mannschaft
        AltsystemUebersetzungDO schuetzeUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,
                Long.valueOf(altsystemDataObject.getSchuetze_Id()));

        // Exception, falls es für den Schützen kein zugehöriges DSB Mitglied gibt
        if (schuetzeUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "No translation found for entity Schuetze to a corresponding DSBMitglied");
        }

        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_Mannschaft,
                Long.valueOf(altsystemDataObject.getSchuetze_Id()));

        // Exception, falls zu dem Schützen keine zugehörige Mannschaft gespeichert wurde
        if (mannschaftUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "No translation found for entity Schuetze to a corresponding Mannschaft");
        }

        // wählt in den Matches der Mannschaft das mit entsprechender MatchNr
        List<MatchDO> matches = matchComponent.findByMannschaftId(mannschaftUebersetzung.getBogenligaId());
        MatchDO match = null;
        for (MatchDO currentMatch : matches){
            if(currentMatch.getNr() == altsystemDataObject.getMatch()){
                match = currentMatch;
                break;
            }
        }

        // Exception, falls kein passendes Match gefunden wurde
        if (match == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Match not found");
        }

        // findet für das Match die Anzahl der Sätze über Value in der Übersetzungstabelle
        AltsystemUebersetzungDO satzUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Match_Saetze, match.getId());
        int anzahlSaetze = Integer.parseInt(satzUebersetzung.getWert());

        int[][] punkte = getPassenpunkte(altsystemDataObject.getErgebnis(), anzahlSaetze);

        for(int i = 0; i < anzahlSaetze; i++){
            PasseDO passe = new PasseDO();
            passe.setPasseLfdnr((long) (i + 1));
            passe.setPasseDsbMitgliedId(schuetzeUebersetzung.getBogenligaId());
            passe.setPasseMannschaftId(match.getMannschaftId());
            passe.setPasseMatchNr(match.getNr());
            passe.setPasseMatchId(match.getId());
            passe.setPasseWettkampfId(match.getWettkampfId());
            passe.setPfeil1(punkte[i][0]);
            passe.setPfeil2(punkte[i][1]);
            passen.add(passe);
        }

        return passen;
    }

    /**
     Recalculates the passen that have been previously created
     Is called when a new Ergebnis-value is entered in the legacy system for an Ergebnis dataset which has previously been synchronized to the new system and should be updated
     @param passen existing Passe objects
     @param altsystemDataObject ergebnis data
     @return List of Passe objects (updated)
     */
    public List<PasseDO> recalculatePassen(List<PasseDO> passen, AltsystemErgebnisseDO altsystemDataObject){

        int[][] punkte = getPassenpunkte(altsystemDataObject.getErgebnis(), passen.size());
        for(int i = 0; i < passen.size(); i++){
            PasseDO passe = passen.get(i);
            passe.setPfeil1(punkte[i][0]);
            passe.setPfeil2(punkte[i][1]);
        }
        return passen;
    }
}
