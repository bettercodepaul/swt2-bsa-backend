package de.bogenliga.application.services.v1.sync.model;

import java.util.List;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *
 * @author Jonas Sigloch
 */
public class SyncWrapper implements DataTransferObject {
    private List<LigaSyncMatchDTO> match;
    private List<LigaSyncPasseDTO> passe;
    private List<LigaSyncMannschaftsmitgliedDTO> mannschaftsmitglied;
    private String offlineToken;
    private long wettkampfId;

    public SyncWrapper(List<LigaSyncMatchDTO> ligaSyncMatchDTOList,
                       List<LigaSyncPasseDTO> ligaSyncPasseDTOS,
                       List<LigaSyncMannschaftsmitgliedDTO> ligaSyncMannschaftsmitgliedDTOS,
                       String offlineToken,
                       long wettkampfId) {
        this.match = ligaSyncMatchDTOList;
        this.passe = ligaSyncPasseDTOS;
        this.mannschaftsmitglied = ligaSyncMannschaftsmitgliedDTOS;
        this.offlineToken = offlineToken;
        this.wettkampfId = wettkampfId;
    }


    public List<LigaSyncMatchDTO> getMatch() {
        return match;
    }


    public void setMatch(List<LigaSyncMatchDTO> match) {
        this.match = match;
    }


    public List<LigaSyncPasseDTO> getPasse() {
        return passe;
    }


    public void setPasse(List<LigaSyncPasseDTO> passe) {
        this.passe = passe;
    }


    public List<LigaSyncMannschaftsmitgliedDTO> getMannschaftsmitglied() {
        return mannschaftsmitglied;
    }


    public void setMannschaftsmitglied(
            List<LigaSyncMannschaftsmitgliedDTO> mannschaftsmitglied) {
        this.mannschaftsmitglied = mannschaftsmitglied;
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
}
