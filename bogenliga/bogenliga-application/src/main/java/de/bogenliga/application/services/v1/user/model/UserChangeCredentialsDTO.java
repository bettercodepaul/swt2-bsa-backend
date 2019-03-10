package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Zum aktualiseren des Passworts eines Anwenders hält diese Klasse
 * sowohl das aktuelle, als auch das neue Passwort
 *
 *
 * @author Michael Dirksmöller, eXXcellent solutions consulting & software gmbh
 */
public class UserChangeCredentialsDTO implements DataTransferObject {

    private static final long serialVersionUID = 7100904135169446743L;
    private String password;
    private String newPassword;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewpassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
