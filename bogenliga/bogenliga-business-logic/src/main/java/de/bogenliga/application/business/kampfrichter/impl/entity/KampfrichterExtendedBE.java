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
    private Long kampfrichterExtendedUserID;
    private Long kampfrichterExtendedWettkampfID;
    private Boolean kampfrichterExtendedLeitend;
    private String kampfrichterExtendedVorname;
    private String kampfrichterExtendedNachname;
    private String kampfrichterExtendedEmail;


    public KampfrichterExtendedBE() {
    }

    public KampfrichterExtendedBE(Long kampfrichterExtendedUserID, Long kampfrichterExtendedWettkampfID,
                                  Boolean kampfrichterExtendedLeitend, String kampfrichterExtendedVorname,
                                  String kampfrichterExtendedNachname, String kampfrichterExtendedEmail) {


        this.kampfrichterExtendedUserID = kampfrichterExtendedUserID;
        this.kampfrichterExtendedWettkampfID = kampfrichterExtendedWettkampfID;
        this.kampfrichterExtendedLeitend = kampfrichterExtendedLeitend;
        this.kampfrichterExtendedVorname = kampfrichterExtendedVorname;
        this.kampfrichterExtendedNachname = kampfrichterExtendedNachname;
        this.kampfrichterExtendedEmail = kampfrichterExtendedEmail;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getKampfrichterExtendedUserID() {
        return kampfrichterExtendedUserID;
    }


    public void setKampfrichterExtendedUserID(Long kampfrichterExtendedUserID) {
        this.kampfrichterExtendedUserID = kampfrichterExtendedUserID;
    }


    public Long getKampfrichterExtendedWettkampfID() {
        return kampfrichterExtendedWettkampfID;
    }


    public void setKampfrichterExtendedWettkampfID(Long kampfrichterExtendedWettkampfID) {
        this.kampfrichterExtendedWettkampfID = kampfrichterExtendedWettkampfID;
    }


    public Boolean getKampfrichterExtendedLeitend() {
        return kampfrichterExtendedLeitend;
    }


    public void setKampfrichterExtendedLeitend(Boolean kampfrichterExtendedLeitend) {
        this.kampfrichterExtendedLeitend = kampfrichterExtendedLeitend;
    }


    public String getKampfrichterExtendedVorname() {
        return kampfrichterExtendedVorname;
    }


    public void setKampfrichterExtendedVorname(String kampfrichterExtendedVorname) {
        this.kampfrichterExtendedVorname = kampfrichterExtendedVorname;
    }


    public String getKampfrichterExtendedNachname() {
        return kampfrichterExtendedNachname;
    }


    public void setKampfrichterExtendedNachname(String kampfrichterExtendedNachname) {
        this.kampfrichterExtendedNachname = kampfrichterExtendedNachname;
    }


    public String getKampfrichterExtendedEmail() {
        return kampfrichterExtendedEmail;
    }


    public void setKampfrichterExtendedEmail(String kampfrichterExtendedEmail) {
        this.kampfrichterExtendedEmail = kampfrichterExtendedEmail;
    }


    @Override
    public String toString() {
        return "KampfrichterExtendedBE{" +
                "KampfrichterExtendedUserID=" + kampfrichterExtendedUserID +
                ", KampfrichterExtendedWettkampfID=" + kampfrichterExtendedWettkampfID +
                ", KampfrichterExtendedLeitend=" + kampfrichterExtendedLeitend +
                ", KampfrichterExtendedVorname='" + kampfrichterExtendedVorname + '\'' +
                ", KampfrichterExtendedNachname='" + kampfrichterExtendedNachname + '\'' +
                ", KampfrichterExtendedEmail='" + kampfrichterExtendedEmail + '\'' +
                '}';
    }
}
