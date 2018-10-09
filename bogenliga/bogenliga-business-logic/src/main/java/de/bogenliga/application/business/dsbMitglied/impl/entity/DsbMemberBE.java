package de.bogenliga.application.business.dsbMember.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the dsbMember business entity.
 * <p>
 * A dsbMember is a registered member of the DSB. The dsbMember is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class DsbMemberBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private long dsbMemberId;
    private String dsbMemberForename;
    private String dsbMemberSurname;
    private String dsbMemberBirthdate;
    private String dsbMemberNationality;
    private String dsbMemberMemberNumber;
    private long dsbMemberClubId;
    private long dsbMemberUserId;


    public DsbMemberBE(){
        // empty constructor
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getDsbMemberId() {
        return dsbMemberId;
    }

    public void setDsbMemberId(long dsbMemberId) {
        this.dsbMemberId = dsbMemberId;
    }

    public String getDsbMemberForename() {
        return dsbMemberForename;
    }

    public void setDsbMemberForename(String dsbMemberForename) {
        this.dsbMemberForename = dsbMemberForename;
    }

    public String getDsbMemberSurname() {
        return dsbMemberSurname;
    }

    public void setDsbMemberSurname(String dsbMemberSurname) {
        this.dsbMemberSurname = dsbMemberSurname;
    }

    public String getDsbMemberBirthdate() {
        return dsbMemberBirthdate;
    }

    public void setDsbMemberBirthdate(String dsbMemberBirthdate) {
        this.dsbMemberBirthdate = dsbMemberBirthdate;
    }

    public String getDsbMemberNationality() {
        return dsbMemberNationality;
    }

    public void setDsbMemberNationality(String dsbMemberNationality) {
        this.dsbMemberNationality = dsbMemberNationality;
    }

    public String getDsbMemberMemberNumber() {
        return dsbMemberMemberNumber;
    }

    public void setDsbMemberMemberNumber(String dsbMemberMemberNumber) {
        this.dsbMemberMemberNumber = dsbMemberMemberNumber;
    }

    public long getDsbMemberClubId() {
        return dsbMemberClubId;
    }

    public void setDsbMemberClubId(long dsbMemberClubId) {
        this.dsbMemberClubId = dsbMemberClubId;
    }

    public long getDsbMemberUserId() {
        return dsbMemberUserId;
    }

    public void setDsbMemberUserId(long dsbMemberUserId) {
        this.dsbMemberUserId = dsbMemberUserId;
    }

    @Override
    public String toString() {
        return "DsbMemberBE{" +
                "dsbMemberId=" + dsbMemberId +
                ", dsbMemberForename='" + dsbMemberForename + '\'' +
                ", dsbMemberSurname='" + dsbMemberSurname + '\'' +
                ", dsbMemberBirthdate='" + dsbMemberBirthdate + '\'' +
                ", dsbMemberNationality='" + dsbMemberNationality + '\'' +
                ", dsbMemberMemberNumber='" + dsbMemberMemberNumber + '\'' +
                ", dsbMemberClubId=" + dsbMemberClubId +
                ", dsbMemberUserId=" + dsbMemberUserId +
                '}';
    }
}
