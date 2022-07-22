package de.bogenliga.application.services.v1.sync.model;

import java.util.List;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.match.model.MatchDTO;

/**
 * I wrap request body of the synchronization POST request that contains
 * {@link List<MatchDTO>}, {@link List<MannschaftsMitgliedDTO>}
 * offline token as string and the wettkampfID as long
 * @author Jonas Sigloch
 */
public class SyncWrapper implements DataTransferObject {
    private long wettkampfId;
    private String offlineToken;
    private List<MatchDTO> matchesDTO;
    private List<MannschaftsMitgliedDTO> mannschaftsmitgliederDTO;

    public SyncWrapper(long wettkampfId,
                       String offlineToken,
                       List<MatchDTO> matchesDTO,
                       List<MannschaftsMitgliedDTO> mannschaftsmitgliederDTO
                       ) {
        this.matchesDTO = matchesDTO;
        this.mannschaftsmitgliederDTO = mannschaftsmitgliederDTO;
        this.offlineToken = offlineToken;
        this.wettkampfId = wettkampfId;
    }


    public List<MatchDTO> getMatch() {
        return matchesDTO;
    }


    public void setMatch(List<MatchDTO> match) {
        this.matchesDTO = match;
    }


     public List<MannschaftsMitgliedDTO> getMannschaftsmitglied() {
        return mannschaftsmitgliederDTO;
    }


    public void setMannschaftsmitglied(
            List<MannschaftsMitgliedDTO> mannschaftsmitglied) {
        this.mannschaftsmitgliederDTO = mannschaftsmitglied;
    }


    public String getOfflineToken() {
        return offlineToken;
    }


    public void setOfflineToken(String offlineToken) {
        this.offlineToken = offlineToken;
    }


    public long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    @Override
    public String toString() {
        return "SyncWrapper{" +
                "matchesDTO=" + matchesDTO +
                ", mannschaftsmitgliederDTO=" + mannschaftsmitgliederDTO +
                ", offlineToken='" + offlineToken + '\'' +
                ", wettkampfId=" + wettkampfId +
                '}';
    }
}
