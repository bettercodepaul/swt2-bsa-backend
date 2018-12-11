package de.bogenliga.application.business.liga.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import com.sun.scenario.effect.Offset;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
public class LigaDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */
    private Long id;
    private String name;
    private Long region_id;
    private String region_name;
    private Long liga_ubergeordnet_id;
    private String liga_uebergeordnet_name;
    private Long liga_verantwortlich_id;
    private String liga_verantwortlich_mail;
    private Long userId;


    /**
     * Constructor with optional parameters
     *
     * @param id
     * @param name
     * @param region_id
     * @param region_name
     * @param liga_ubergeordnet_id
     * @param liga_uebergeordnet_name
     * @param liga_verantwortlich_id
     * @param liga_verantwortlich_mail
     * @param userId
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */
    public LigaDO(final Long id, final String name, final Long region_id, final String region_name,
                  final Long liga_ubergeordnet_id, final String liga_uebergeordnet_name,
                  final Long liga_verantwortlich_id, final String liga_verantwortlich_mail, Long userId,
                  final OffsetDateTime createdAtUtc, final Long createdByUserId,
                  final OffsetDateTime lastModifiedAtUtc, final Long lastModifiedByUserId,
                  final Long version) {
        this.id = id;
        this.name = name;
        this.region_id = region_id;
        this.region_name = region_name;
        this.liga_ubergeordnet_id = liga_ubergeordnet_id;
        this.liga_uebergeordnet_name = liga_uebergeordnet_name;
        this.liga_verantwortlich_id = liga_verantwortlich_id;
        this.liga_verantwortlich_mail = liga_verantwortlich_mail;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Constructor with mandatory parameters
     *
     * @param id
     * @param name
     * @param region_id
     * @param region_name
     * @param liga_ubergeordnet_id
     * @param liga_uebergeordnet_name
     * @param liga_verantwortlich_id
     * @param liga_verantwortlich_mail
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public LigaDO(final Long id, final String name, final Long region_id, final String region_name,
                  final Long liga_ubergeordnet_id, final String liga_uebergeordnet_name,
                  final Long liga_verantwortlich_id, final String liga_verantwortlich_mail,
                  final OffsetDateTime createdAtUtc, Long createdByUserId, final Long version) {
        this.id = id;
        this.name = name;
        this.region_id = region_id;
        this.region_name = region_name;
        this.liga_ubergeordnet_id = liga_ubergeordnet_id;
        this.liga_uebergeordnet_name = liga_uebergeordnet_name;
        this.liga_verantwortlich_id = liga_verantwortlich_id;
        this.liga_verantwortlich_mail = liga_verantwortlich_mail;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    /**
     * Constructor without technical parameters
     *
     * @param id
     * @param name
     * @param region_id
     * @param region_name
     * @param liga_ubergeordnet_id
     * @param liga_uebergeordnet_name
     * @param liga_verantwortlich_id
     * @param liga_verantwortlich_mail
     */
    public LigaDO(final Long id, final String name, final Long region_id, final String region_name,
                  final Long liga_ubergeordnet_id, final String liga_uebergeordnet_name,
                  final Long liga_verantwortlich_id, final String liga_verantwortlich_mail) {
        this.id = id;
        this.name = name;
        this.region_id = region_id;
        this.region_name = region_name;
        this.liga_ubergeordnet_id = liga_ubergeordnet_id;
        this.liga_uebergeordnet_name = liga_uebergeordnet_name;
        this.liga_verantwortlich_id = liga_verantwortlich_id;
        this.liga_verantwortlich_mail = liga_verantwortlich_mail;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getRegion_id() {
        return region_id;
    }


    public void setRegion_id(Long region_id) {
        this.region_id = region_id;
    }


    public String getRegion_name() {
        return region_name;
    }


    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }


    public Long getLiga_ubergeordnet_id() {
        return liga_ubergeordnet_id;
    }


    public void setLiga_ubergeordnet_id(Long liga_ubergeordnet_id) {
        this.liga_ubergeordnet_id = liga_ubergeordnet_id;
    }


    public String getLiga_uebergeordnet_name() {
        return liga_uebergeordnet_name;
    }


    public void setLiga_uebergeordnet_name(String liga_uebergeordnet_name) {
        this.liga_uebergeordnet_name = liga_uebergeordnet_name;
    }


    public Long getLiga_verantwortlich_id() {
        return liga_verantwortlich_id;
    }


    public void setLiga_verantwortlich_id(Long liga_verantwortlich_id) {
        this.liga_verantwortlich_id = liga_verantwortlich_id;
    }


    public String getLiga_verantwortlich_mail() {
        return liga_verantwortlich_mail;
    }


    public void setLiga_verantwortlich_mail(String liga_verantwortlich_mail) {
        this.liga_verantwortlich_mail = liga_verantwortlich_mail;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, region_id, region_name, liga_ubergeordnet_id,
                liga_uebergeordnet_name, liga_verantwortlich_id, liga_verantwortlich_mail,
                userId, createdByUserId, lastModifiedAtUtc, getLastModifiedByUserId(), version);
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LigaDO that = (LigaDO) o;
        return id == that.id &&
                userId==that.userId&&
                region_id == that.region_id &&
                liga_ubergeordnet_id==that.liga_ubergeordnet_id &&
                liga_verantwortlich_id == that.liga_verantwortlich_id &&
                Objects.equals(name, that.name)&&
                Objects.equals(region_name,that.region_name)&&
                Objects.equals(liga_uebergeordnet_name,that.liga_uebergeordnet_name)&&
                Objects.equals(liga_verantwortlich_mail,that.liga_verantwortlich_mail)&&
                Objects.equals(lastModifiedAtUtc,that.lastModifiedAtUtc);

    }

}