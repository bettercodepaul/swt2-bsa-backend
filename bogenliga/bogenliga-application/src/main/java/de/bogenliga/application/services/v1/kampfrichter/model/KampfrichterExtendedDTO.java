package de.bogenliga.application.services.v1.kampfrichter.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *
 * Extended KampfrichterDTO class to KampfrichterExtendedDTO. The difference are the Attributes: "kampfrichterVorname", "kampfrichterNachname"
 * and email.
 *
 */
public class KampfrichterExtendedDTO implements DataTransferObject {
    private static final Long serialVersionUID = 8559563978424033907L;
    private Long userID;
    private Long wettkampfID;
    private String kampfrichterVorname;
    private String kampfrichterNachname;
    private String email;
    private boolean leitend;


    // The parameter names have to be the exact same as the attribute names in kampfrichter-do.class.ts
    public KampfrichterExtendedDTO(final Long id, final String kampfrichterVorname, final String kampfrichterNachname, final String email, final Long wettkampfID, final boolean leitend) {
        this.userID = id;
        this.kampfrichterVorname = kampfrichterVorname;
        this.kampfrichterNachname = kampfrichterNachname;
        this.email= email;
        this.wettkampfID = wettkampfID;
        this.leitend = leitend;
    }


    public Long getUserID() {
        return userID;
    }


    public void setUserId(final Long userId) {
        this.userID = userId;
    }


    public Long getWettkampfID() {
        return wettkampfID;
    }


    public void setWettkampfId(final Long wettkampfId) {
        this.wettkampfID = wettkampfId;
    }


    public boolean getLeitend() {
        return leitend;
    }


    public void setLeitend(final boolean leitend) {
        this.leitend = leitend;
    }


    public String getKampfrichterVorname() {
        return kampfrichterVorname;
    }


    public void setKampfrichterVorname(String kampfrichterVorname) {
        this.kampfrichterVorname = kampfrichterVorname;
    }


    public String getKampfrichterNachname() {
        return kampfrichterNachname;
    }


    public void setKampfrichterNachname(String kampfrichterNachname) {
        this.kampfrichterNachname = kampfrichterNachname;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "KampfrichterExtendedDTO{" +
                "userID=" + userID +
                ", wettkampfID=" + wettkampfID +
                ", kampfrichterVorname='" + kampfrichterVorname + '\'' +
                ", kampfrichterNachname='" + kampfrichterNachname + '\'' +
                ", email='" + email + '\'' +
                ", leitend=" + leitend +
                '}';
    }
}

