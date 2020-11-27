package de.bogenliga.application.services.v1.einstellungen.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public class EinstellungenDTO implements DataTransferObject {

    private Long id;

    private String smtpName;
    private String smtpHost;
    private Integer smtpPort;
    private String smtpPasswort;


    public EinstellungenDTO(Long id, String name, String host, Integer port, String passwort ) {
        this.id = id;
        this.smtpName = name;
        this.smtpHost = host;
        this.smtpPort = port;
        this.smtpPasswort = passwort;
    }

    public EinstellungenDTO() {
        //leerr Konstruktor
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setSmtpName(String name) {
        this.smtpName = name;
    }
    public void setSmtpHost(String host) {
        this.smtpHost = host;
    }
    public void setSmtpPort(Integer port) {
        this.smtpPort = port;
    }
    public void setSmtpPasswort(String passwort) {
        this.smtpPasswort = passwort;
    }

    public Long getId() {
        return id;
    }

    public String getSmtpName() {
        return smtpName;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpPasswort() {
        return smtpPasswort;
    }
}
