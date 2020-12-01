package de.bogenliga.application.services.v1.einstellungen.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public class EinstellungenDTO implements DataTransferObject {

    private Long id;

    private String value;
    private String key;


    public EinstellungenDTO(Long id, String value, String key) {
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
    public void setValue(String value) {
        this.value = value;
    }
    public void setSmtpHost(String host) {
        this.key = key;
    }


    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

}
