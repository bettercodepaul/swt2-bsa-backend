package de.bogenliga.application.services.v1.liga.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the liga.
 * <p>
 * I define the payload for the external REST interface of the liga business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaDTO implements DataTransferObject {

    private Long id;
    private String name;
    private Long region_id;
    private String region_name;
    private Long liga_ubergeordnet_id;
    private String liga_uebergeordnet_name;
    private Long liga_verantwortlich_id;
    private String liga_verantwortlich_mail;


    /**
     * @param id
     * @param name
     * @param region_id
     * @param region_name
     * @param liga_ubergeordnet_id
     * @param liga_uebergeordnet_name
     * @param liga_verantwortlich_id
     * @param liga_verantwortlich_mail
     */
    public LigaDTO(Long id, String name, Long region_id, String region_name, Long liga_ubergeordnet_id,
                   String liga_uebergeordnet_name, Long liga_verantwortlich_id, String liga_verantwortlich_mail) {
        this.id = id;
        this.name = name;
        this.region_id = region_id;
        this.region_name = region_name;
        this.liga_ubergeordnet_id = liga_ubergeordnet_id;
        this.liga_uebergeordnet_name = liga_uebergeordnet_name;
        this.liga_verantwortlich_id = liga_verantwortlich_id;
        this.liga_verantwortlich_mail = liga_verantwortlich_mail;
    }


    public LigaDTO() {
        // empty constructor
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
}
