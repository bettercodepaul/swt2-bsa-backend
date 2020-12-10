package de.bogenliga.application.services.v1.einstellungen.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public class EinstellungenDTO implements DataTransferObject {

    private String value;
    private String key;


    public EinstellungenDTO(String value, String key) {
        this.value = value;
        this.key = key;

    }

    public EinstellungenDTO() {
        //leerer Konstruktor
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setSmtpHost(String host) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

}
