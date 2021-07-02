package de.bogenliga.application.business.kampfrichter.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the kampfrichter business entity.
 * <p>
 * A kampfrichter is a registered member of the DSB. The kampfrichter is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class KampfrichterExtendedBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long KampfrichterExtendedUserID;
    private Long KampfrichterExtendedWettkampfID;
    private Boolean KampfrichterExtendedLeitend;
    private String KampfrichterExtendedVorname;
    private String KampfrichterExtendedNachname;
    private String KampfrichterExtendedEmail;


    public KampfrichterExtendedBE() {
    }

    public KampfrichterExtendedBE(Long kampfrichterExtendedUserID, Long kampfrichterExtendedWettkampfID,
                                  Boolean kampfrichterExtendedLeitend, String kampfrichterExtendedVorname,
                                  String kampfrichterExtendedNachname, String kampfrichterExtendedEmail) {
        KampfrichterExtendedUserID = kampfrichterExtendedUserID;
        KampfrichterExtendedWettkampfID = kampfrichterExtendedWettkampfID;
        KampfrichterExtendedLeitend = kampfrichterExtendedLeitend;
        KampfrichterExtendedVorname = kampfrichterExtendedVorname;
        KampfrichterExtendedNachname = kampfrichterExtendedNachname;
        KampfrichterExtendedEmail = kampfrichterExtendedEmail;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getKampfrichterExtendedUserID() {
        return KampfrichterExtendedUserID;
    }


    public void setKampfrichterExtendedUserID(Long kampfrichterExtendedUserID) {
        KampfrichterExtendedUserID = kampfrichterExtendedUserID;
    }


    public Long getKampfrichterExtendedWettkampfID() {
        return KampfrichterExtendedWettkampfID;
    }


    public void setKampfrichterExtendedWettkampfID(Long kampfrichterExtendedWettkampfID) {
        KampfrichterExtendedWettkampfID = kampfrichterExtendedWettkampfID;
    }


    public Boolean getKampfrichterExtendedLeitend() {
        return KampfrichterExtendedLeitend;
    }


    public void setKampfrichterExtendedLeitend(Boolean kampfrichterExtendedLeitend) {
        KampfrichterExtendedLeitend = kampfrichterExtendedLeitend;
    }


    public String getKampfrichterExtendedVorname() {
        return KampfrichterExtendedVorname;
    }


    public void setKampfrichterExtendedVorname(String kampfrichterExtendedVorname) {
        KampfrichterExtendedVorname = kampfrichterExtendedVorname;
    }


    public String getKampfrichterExtendedNachname() {
        return KampfrichterExtendedNachname;
    }


    public void setKampfrichterExtendedNachname(String kampfrichterExtendedNachname) {
        KampfrichterExtendedNachname = kampfrichterExtendedNachname;
    }


    public String getKampfrichterExtendedEmail() {
        return KampfrichterExtendedEmail;
    }


    public void setKampfrichterExtendedEmail(String kampfrichterExtendedEmail) {
        KampfrichterExtendedEmail = kampfrichterExtendedEmail;
    }


    @Override
    public String toString() {
        return "KampfrichterExtendedBE{" +
                "KampfrichterExtendedUserID=" + KampfrichterExtendedUserID +
                ", KampfrichterExtendedWettkampfID=" + KampfrichterExtendedWettkampfID +
                ", KampfrichterExtendedLeitend=" + KampfrichterExtendedLeitend +
                ", KampfrichterExtendedVorname='" + KampfrichterExtendedVorname + '\'' +
                ", KampfrichterExtendedNachname='" + KampfrichterExtendedNachname + '\'' +
                ", KampfrichterExtendedEmail='" + KampfrichterExtendedEmail + '\'' +
                '}';
    }
}
