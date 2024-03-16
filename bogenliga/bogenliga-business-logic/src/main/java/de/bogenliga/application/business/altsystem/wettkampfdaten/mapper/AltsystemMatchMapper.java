package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import java.util.List;

import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;

/**
 * Mappt Wettkampfdaten aus dem Altsystem auf Matches im neuen System
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMatchMapper {

    private final MatchComponent matchComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;
    @Autowired
    public AltsystemMatchMapper(MatchComponent matchComponent, VeranstaltungComponent veranstaltungComponent, AltsystemUebersetzung altsystemUebersetzung){
        this.matchComponent = matchComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     Extracts data from the legacy dataset and stores them in MatchDO objects
     Sets the values for two MatchDO objects. One for the home team, one for the opponent*
     @param matchDO array of size 2 with empty matchDOs for the home team and opponent (size: 2)
     @param altsystemDataObject legacy data to this match
     @return modified array of MatchDO with mapped entities
     */
    public MatchDO toDO(MatchDO matchDO, AltsystemWettkampfdatenDO altsystemDataObject) {
        // Create a DataObject for the match
        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getMannschaft());
        matchDO.setMannschaftId(mannschaftUebersetzung.getBogenligaId());
        matchDO.setSatzpunkte((long) altsystemDataObject.getSatzPlus());
        matchDO.setMatchpunkte((long) altsystemDataObject.getMatchPlus());
        matchDO.setNr((long) altsystemDataObject.getMatch());


        return matchDO;
    }


    /**
     Adds fields to the new LigaDO which cannot be extracted from the legacy data and therefore must be set to a default value*
     @param matchDO array of size 2 with the matchDOs for the home team and opponent (size: 2)
     @param wettkampfTage list of WettkampfDOs existing for the Veranstaltung of the team
     @return modified array of MatchDO
     */
    public MatchDO addDefaultFields(MatchDO matchDO, List<WettkampfDO> wettkampfTage) {

        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfTage.get(0).getWettkampfVeranstaltungsId());

        WettkampfDO wettkampfDO = getCurrentWettkampfTag(matchDO.getNr(), veranstaltungDO.getVeranstaltungGroesse(),wettkampfTage);

        // hier wird die MatchNr überschrieben....
         matchDO.setNr(getCurrentBsappMatch(matchDO.getNr(),  veranstaltungDO.getVeranstaltungGroesse()));

        long matchCount = getMatchCountForWettkampf(wettkampfDO);
        Long currentScheibenNummer;
        Long currentBegegnung;
        // für den Fall, dass noch keine Matches existieren - dann ist die Vorbelegung so, dass
        // Scheibe =1 und Begegnung =1 herauskommmen.
        // Scheibennummer und aktuelle Begegnung anhand der Anzahl aller Matches errechnen
        currentScheibenNummer = (matchCount % veranstaltungDO.getVeranstaltungGroesse())+1;
        currentBegegnung = (currentScheibenNummer + 1) / 2;

        // Defaultfelder setzen
        matchDO.setWettkampfId(wettkampfDO.getId());
        matchDO.setMatchScheibennummer(currentScheibenNummer);
        matchDO.setBegegnung(currentBegegnung);
        // Alle Strafpunkte auf 0 setzen
        matchDO.setStrafPunkteSatz1(0L);
        matchDO.setStrafPunkteSatz2(0L);
        matchDO.setStrafPunkteSatz3(0L);
        matchDO.setStrafPunkteSatz4(0L);
        matchDO.setStrafPunkteSatz5(0L);
        return matchDO;
    }

    /**
     Helper function to determine the corresponding Wettkampf for a match*
     @param matchNummer Nummer des aktuellen Matches for which a wettkampf should be determined
     @param veranstaltungsgroesse Anzahl der Mannschaften n dieser Veranstaltung
     @param wettkampfTage list of all Wettkampf objects existing for the Veranstaltung of the Mannschaft
     @return WettkampfDO of the corresponding Wettkampf
     */
    public WettkampfDO getCurrentWettkampfTag(Long matchNummer, int veranstaltungsgroesse, List<WettkampfDO> wettkampfTage){
        WettkampfDO currentWettkampfTag;

        // Bestimmen des zugehörigen Wettkampftages
       int  currentIndexWettkampfTag = (int) ((matchNummer-1) / (veranstaltungsgroesse-1));
       //die Abbildung muss sein:
        // 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9 - 10 - 11 - 12 - 13 - 14 - 15...
        // 0 - 0 - 0 - 0 - 0 - 0 - 0 - 1 - 1 - 1  - 1  - 1  - 1  - 1  - 2...
       if(currentIndexWettkampfTag< wettkampfTage.size()-1) {
           currentWettkampfTag = wettkampfTage.get(currentIndexWettkampfTag);
       }
       else{
           currentWettkampfTag = wettkampfTage.get(wettkampfTage.size()-1);
       }

        return currentWettkampfTag;
    }

    public Long getCurrentBsappMatch(Long matchNummer, int veranstaltungsGroesse){
        Long matchNr;
        matchNr = (matchNummer % (veranstaltungsGroesse-1L));
        if (matchNr == 0L){
            matchNr =  (veranstaltungsGroesse-1L);
        }
        return matchNr;
    }
    /**
     Helper function to calculate the number of match-datasets existing for a Wettkampf
     With the number of datasets the values for the fields Begegnung and Scheibennummer can be calculated*
     @param wettkampfDO Dataobject of the corresponding Wettkampf
     @return number of match-datasets (not number of actual matches, because for each match 2 datasets are stored)
     */
    public int getMatchCountForWettkampf(WettkampfDO wettkampfDO){
        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfDO.getId());
        return matches.size();
    }
}
