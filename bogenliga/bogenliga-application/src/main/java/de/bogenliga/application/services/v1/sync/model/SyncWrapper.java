package de.bogenliga.application.services.v1.sync.model;

import java.util.List;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I wrap request body of the synchronization POST request that contains
 * {@link List<LigaSyncMatchDTO>}, {@link List<LigaSyncPasseDTO>}, {@link List<LigaSyncMannschaftsmitgliedDTO>}
 * offline token as string and the wettkampfID as long
 * @author Jonas Sigloch
 */
public class SyncWrapper implements DataTransferObject {
    private List<LigaSyncMatchDTO> match;
    private List<LigaSyncPasseDTO> passe;
    private List<LigaSyncMannschaftsmitgliedDTO> mannschaftsMitglieder;
    private String offlineToken;
    private long wettkampfId;

    public SyncWrapper(List<LigaSyncMatchDTO> matchesDTO,
                       List<LigaSyncPasseDTO> passe,
                       List<LigaSyncMannschaftsmitgliedDTO> mannschaftsmitgliederDTO ,
                       String offlineToken,
                       long wettkampfId) {
        this.match = matchesDTO;
        this.passe = passe;
        this.mannschaftsMitglieder = mannschaftsmitgliederDTO ;
        this.offlineToken = offlineToken;
        this.wettkampfId = wettkampfId;
    }


    public List<LigaSyncMatchDTO> getMatch() {
        return this.match;
    }


    public void setMatch(List<LigaSyncMatchDTO> match) {
        this.match = match;
    }


    public List<LigaSyncPasseDTO> getPasse() {
        return this.passe;
    }


    public void setPasse(List<LigaSyncPasseDTO> passe) {
        this.passe = passe;
    }


    public List<LigaSyncMannschaftsmitgliedDTO> getMannschaftsmitglied() {
        return this.mannschaftsMitglieder;
    }


    public void setMannschaftsmitglied(
            List<LigaSyncMannschaftsmitgliedDTO> mannschaftsmitglied) {
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
                ", passe=" + passe +
                ", mannschaftsMitglieder=" + mannschaftsMitglieder +
                ", offlineToken='" + offlineToken + '\'' +
                ", wettkampfId=" + wettkampfId +
                '}';
    }
}
