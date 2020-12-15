package de.bogenliga.application.services.v1.einstellungen.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the einstellungen.
 * <p>
 * I define the payload for the external REST interface of the einstellungen business entity.
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public class EinstellungenDTO implements DataTransferObject {

    private Long id;
    private String value;
    private String key;


    /**
     * Constructors
     */
    public EinstellungenDTO(Long id, String key, String value) {
        this.id = id;
        this.value = value;
        this.key = key;

    }


    public EinstellungenDTO() {
        //leerer Konstruktor
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return this.id;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getValue() {
        return value;
    }


    public String getKey() {
        return key;
    }
}
