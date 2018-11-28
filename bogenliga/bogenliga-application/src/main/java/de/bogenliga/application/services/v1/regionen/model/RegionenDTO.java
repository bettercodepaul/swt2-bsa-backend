package de.bogenliga.application.services.v1.regionen.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I map the {@link RegionenDO} and {@link RegionenDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class RegionenDTO implements DataTransferObject {
    private Long id;
    private String name;
    private String kuerzel;
    private String typ;
    private Long uebergeordnet;
    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;



    /**
     * Constructors
     */
    public RegionenDTO() {
        //empty
    }


    /**
     *
     * @param id
     * @param name
     * @param kuerzel
     * @param typ
     * @param uebergeordnet
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public RegionenDTO(Long id, String name, String kuerzel, String typ, Long uebergeordnet,
                      OffsetDateTime createdAtUtc, Long createdByUserId, Long version) {
        this.id = id;
        this.name = name;
        this.kuerzel = kuerzel;
        this.typ = typ;
        this.uebergeordnet = uebergeordnet;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
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


    public String getKuerzel() {
        return kuerzel;
    }


    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }


    public String getTyp() {
        return typ;
    }


    public void setTyp(String typ) {
        this.typ = typ;
    }


    public Long getUebergeordnet() {
        return uebergeordnet;
    }


    public void setUebergeordnet(Long uebergeordnet) {
        this.uebergeordnet = uebergeordnet;
    }


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public Long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }
}