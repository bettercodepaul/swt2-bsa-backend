package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import java.util.List;
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
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMatchMapper {

    private final MatchComponent matchComponent;
    @Autowired
    public AltsystemMatchMapper(MatchComponent matchComponent){
        this.matchComponent = matchComponent;
    }

    public MatchDO[] toDO(MatchDO[] match, AltsystemWettkampfdatenDO altsystemDataObject) {
        MatchDO mannschaftDO = match[0];
        AltsystemUebersetzungDO mannschaftUebersetzung = AltsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, (long) altsystemDataObject.getMannschaftId());
        mannschaftDO.setMannschaftId(mannschaftUebersetzung.getBogenliga_id());
        mannschaftDO.setSatzpunkte((long) altsystemDataObject.getSatzPlus());
        mannschaftDO.setMatchpunkte((long) altsystemDataObject.getMatchPlus());
        mannschaftDO.setBegegnung((long) altsystemDataObject.getMatch());

        MatchDO gegnerDO = match[1];
        AltsystemUebersetzungDO gegnerUebersetzung = AltsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, (long) altsystemDataObject.getGegnerId());
        gegnerDO.setMannschaftId(mannschaftUebersetzung.getBogenliga_id());
        gegnerDO.setSatzpunkte((long) altsystemDataObject.getSatzMinus());
        gegnerDO.setMatchpunkte((long) altsystemDataObject.getMatchMinus());
        gegnerDO.setBegegnung((long) altsystemDataObject.getMatch());

        return match;
    }


    public MatchDO[] addDefaultFields(MatchDO[] matchDO, List<WettkampfDO> wettkampfTage) {
        WettkampfDO wettkampfDO = getCurrentWettkampfTag(wettkampfTage);
        for(int i = 0; i < 2; i++){
            matchDO[i].setWettkampfId(wettkampfDO.getId());
            matchDO[i].setMatchScheibennummer((long) getScheibenNummerPointer(wettkampfDO) + i + 1);
            matchDO[i].setStrafPunkteSatz1(0L);
            matchDO[i].setStrafPunkteSatz2(0L);
            matchDO[i].setStrafPunkteSatz3(0L);
            matchDO[i].setStrafPunkteSatz4(0L);
            matchDO[i].setStrafPunkteSatz5(0L);
        }
        return matchDO;
    }

    public WettkampfDO getCurrentWettkampfTag(List<WettkampfDO> wettkampfTage){
        WettkampfDO result = null;
        for(int i = 0; i < 4; i++){
            List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfTage.get(i).getId());
            if(matches.size() < 56){
                result = wettkampfTage.get(i);
                break;
            }
        }
        return result;
    }

    public int getScheibenNummerPointer(WettkampfDO wettkampfDO){
        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfDO.getId());
        int matchCount = matches.size();
        return matchCount % 8;
    }

}
