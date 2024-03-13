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
    public MatchDO[] toDO(MatchDO[] matchDO, AltsystemWettkampfdatenDO altsystemDataObject) {
        // Create a DataObject for the first team
        MatchDO mannschaftDO = matchDO[0];
        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getMannschaft());
        mannschaftDO.setMannschaftId(mannschaftUebersetzung.getBogenligaId());
        mannschaftDO.setSatzpunkte((long) altsystemDataObject.getSatzPlus());
        mannschaftDO.setMatchpunkte((long) altsystemDataObject.getMatchPlus());
        mannschaftDO.setNr((long) altsystemDataObject.getMatch());

        // Create a DataObject for the opponent
        MatchDO gegnerDO = matchDO[1];
        AltsystemUebersetzungDO gegnerUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getGegner());
        gegnerDO.setMannschaftId(gegnerUebersetzung.getBogenligaId());
        gegnerDO.setSatzpunkte((long) altsystemDataObject.getSatzMinus());
        gegnerDO.setMatchpunkte((long) altsystemDataObject.getMatchMinus());
        gegnerDO.setNr((long) altsystemDataObject.getMatch());

        return matchDO;
    }


    /**
     Adds fields to the new LigaDO which cannot be extracted from the legacy data and therefore must be set to a default value*
     @param matchDO array of size 2 with the matchDOs for the home team and opponent (size: 2)
     @param wettkampfTage list of WettkampfDOs existing for the Veranstaltung of the team
     @return modified array of MatchDO
     */
    public MatchDO[] addDefaultFields(MatchDO[] matchDO, List<WettkampfDO> wettkampfTage) {

        WettkampfDO wettkampfDO = getCurrentWettkampfTag(matchDO[0], wettkampfTage);
        matchDO[1].setNr(matchDO[0].getNr());

        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfTage.get(0).getWettkampfVeranstaltungsId());

        long matchCount = getMatchCountForWettkampf(wettkampfDO);
        // für den Fall, dass noch keine Matches existieren - dann ist die Vorbelegung so, dass
        // Scheibe =1 und Begegnung =1 herauskommmen.
        if (matchCount==0L) {
            matchCount = 1L;
        }
        // Scheibennummer und aktuelle Begegnung anhand der Anzahl aller Matches errechnen
        Long currentScheibenNummer = (Long) (matchCount % veranstaltungDO.getVeranstaltungGroesse());
        // für den Fall, dass wir genau bei Scheibe = Vielfaches der Veranstaltungsgröße herauskommen
        // setzen wir Scheibe = 8 (max)
        if (currentScheibenNummer==0L) {
            currentScheibenNummer = 8L;
        }
        Long currentBegegnung = (currentScheibenNummer+1)/2;



        for(int i = 0; i < 2; i++){
            // Defaultfelder setzen
            matchDO[i].setWettkampfId(wettkampfDO.getId());
            matchDO[i].setMatchScheibennummer(currentScheibenNummer + i);
            matchDO[i].setBegegnung(currentBegegnung);
            // Alle Strafpunkte auf 0 setzen
            matchDO[i].setStrafPunkteSatz1(0L);
            matchDO[i].setStrafPunkteSatz2(0L);
            matchDO[i].setStrafPunkteSatz3(0L);
            matchDO[i].setStrafPunkteSatz4(0L);
            matchDO[i].setStrafPunkteSatz5(0L);
        }
        return matchDO;
    }

    /**
     Helper function to determine the corresponding Wettkampf for a match*
     @param matchDO match for which a wettkampf should be determined
     @param wettkampfTage list of all Wettkampf objects existing for the Veranstaltung of the Mannschaft
     @return WettkampfDO of the corresponding Wettkampf
     */
    public WettkampfDO getCurrentWettkampfTag(MatchDO matchDO, List<WettkampfDO> wettkampfTage){
        WettkampfDO currentWettkampfTag;

        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(wettkampfTage.get(0).getWettkampfVeranstaltungsId());

        // Bestimmen des zugehörigen Wettkampftages
       int  currentIndexWettkampfTag = (int) (matchDO.getNr() / veranstaltungDO.getVeranstaltungGroesse());
       if(currentIndexWettkampfTag< wettkampfTage.size()-1) {
           currentWettkampfTag = wettkampfTage.get(currentIndexWettkampfTag);
       }
       else{
           currentWettkampfTag = wettkampfTage.get(wettkampfTage.size()-1);
       }
       matchDO.setNr(matchDO.getNr() % (veranstaltungDO.getVeranstaltungGroesse()-1));
       if (matchDO.getNr() == 0){
           matchDO.setNr(veranstaltungDO.getVeranstaltungGroesse()-1L);
       }
       return currentWettkampfTag;
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
