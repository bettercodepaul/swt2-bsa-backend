package de.bogenliga.application.business.dsbMember.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the dsbMember business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class DsbMemberDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private long id;
    private String forename;
    private String surname;
    private String birthdate;
    private String nationality;
    private String memberNumber;
    private long clubId;
    private long userId;


    /**
     * Constructor with optional parameters
     */
    public DsbMemberDO(long id, String forename, String surname, String birthdate,
                       String nationality, String memberNumber, long clubId, long userId, final OffsetDateTime createdAtUtc,
                       final long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                       final long lastModifiedByUserId, final long version) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.birthdate = birthdate;
        this.nationality = nationality;
        this.memberNumber = memberNumber;
        this.clubId = clubId;
        this.userId = userId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }

    /**
     * Constructor with mandatory parameters
     */
    public DsbMemberDO(long id, String forename, String surname, String birthdate,
                       String nationality, String memberNumber, long clubId, final OffsetDateTime createdAtUtc,
                       final long createdByUserId, final long version) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.birthdate = birthdate;
        this.nationality = nationality;
        this.memberNumber = memberNumber;
        this.clubId = clubId;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public long getClubId() {
        return clubId;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DsbMemberDO that = (DsbMemberDO) o;
        return id == that.id &&
                clubId == that.clubId &&
                userId == that.userId &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(forename, that.forename) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(birthdate, that.birthdate) &&
                Objects.equals(nationality, that.nationality) &&
                Objects.equals(memberNumber, that.memberNumber) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, forename, surname, birthdate, nationality, memberNumber, clubId, userId, createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    }
}
