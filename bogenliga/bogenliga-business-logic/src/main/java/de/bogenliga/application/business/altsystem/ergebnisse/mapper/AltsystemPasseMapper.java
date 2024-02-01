package de.bogenliga.application.business.altsystem.ergebnisse.mapper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemPasseMapper {

    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final MatchComponent matchComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemPasseMapper(final WettkampfComponent wettkampfComponent,
                                VeranstaltungComponent veranstaltungComponent,
                                DsbMitgliedComponent dsbMitgliedComponent,
                                MatchComponent matchComponent,
                                AltsystemUebersetzung altsystemUebersetzung){
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.matchComponent = matchComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    // pro Schütze und Match:
    // Aufteilung der Ergebnispunkte auf durchschnittliche Punkte pro Passe, Restpunkte werden aufgeteilt
    public int[][] getPassenpunkte (long anzahlPunkte, int anzahlSaetze) {
        int[][] punkte = new int[anzahlSaetze][2];

        // Aufteilung des Ergebnisses auf Passenunkte
        int avgErgebnisProPasse = (int) Math.floor(anzahlPunkte / (double) anzahlSaetze);
        int restpunkte = (int) (anzahlPunkte % anzahlSaetze);

        // Aufteilung der Passenpunkte auf jew. 2 Pfeile
        for(int i = 0; i < anzahlSaetze; i++){
            for(int j = 0; j < 2; j++) {
                int ringzahl = avgErgebnisProPasse;
                if (restpunkte > 0 && ringzahl < 10) {
                    ringzahl++;
                    restpunkte--;
                }
                punkte[i][j] = ringzahl;
            }
        }
        return punkte;
    }
    public List<PasseDO> toDO(List<PasseDO> passen, AltsystemErgebnisseDO altsystemDataObject) {

        // Übersetzungstabelle schuetzeID --> DSBMitglied bzw. Mannschaft
        AltsystemUebersetzungDO schuetzeUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_DSBMitglied,
                altsystemDataObject.getSchuetzeID());
        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schütze_Mannschaft,
                altsystemDataObject.getSchuetzeID());

        // wählt in den Matches der Mannschaft das mit entsprechender MatchNr
        List<MatchDO> matches = matchComponent.findByMannschaftId(mannschaftUebersetzung.getBogenligaId());
        MatchDO match = null;
        for (MatchDO currentMatch : matches){
            if(currentMatch.getNr() == altsystemDataObject.getMatch()){
                match = currentMatch;
                break;
            }
        }

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
}
