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
    private List<MatchDTO> match;
    private List<MannschaftsMitgliedDTO> mannschaftsMitglieder;
    private String offlineToken;
    private long wettkampfId;

    public SyncWrapper(List<MatchDTO> match,
                       List<MannschaftsMitgliedDTO> mannschaftsMitglieder,
                       String offlineToken,
                       long wettkampfId) {
        this.match = match;
        this.mannschaftsMitglieder = mannschaftsMitglieder;
        this.offlineToken = offlineToken;
        this.wettkampfId = wettkampfId;
    }


    public List<MatchDTO> getMatch() {
        return match;
    }


    public void setMatch(List<MatchDTO> match) {
        this.match = match;
    }


     public List<MannschaftsMitgliedDTO> getMannschaftsmitglied() {
        return mannschaftsMitglieder;
    }


    public void setMannschaftsmitglied(
            List<MannschaftsMitgliedDTO> mannschaftsmitglied) {
        this.mannschaftsMitglieder = mannschaftsmitglied;
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
                "match=" + match +
                ", mannschaftsMitglieder=" + mannschaftsMitglieder +
                ", offlineToken='" + offlineToken + '\'' +
                ", wettkampfId=" + wettkampfId +
                '}';
    }
}
