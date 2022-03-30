package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Zur initialen Anlage einer neuen Anwenders werden Name und Password gehalten
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public class UserCredentialsDTO implements DataTransferObject {

    private static final long serialVersionUID = 7100904135169446743L;
    private String username;
    private String password;
    private Long dsbMitgliedId;
    private String code;
    private boolean using2FA;


    public String getUsername() {
        return username;
    }


    public void setUsername(final String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(final String password) {
        this.password = password;
    }

    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }
    public void setDsbMitgliedId(final Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public boolean isUsing2FA() {
        return using2FA;
    }


    public void setUsing2FA(boolean using2FA) {
        this.using2FA = using2FA;
    }
}
