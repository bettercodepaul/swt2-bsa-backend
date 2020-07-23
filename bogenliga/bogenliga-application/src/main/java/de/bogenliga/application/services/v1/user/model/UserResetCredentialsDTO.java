package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Zum Resetten des Passworts eines anderen Benutzers entählt die Klasse
 * das neue Passwort
 *
 *
 * @author Michael Dirksmöller, eXXcellent solutions consulting & software gmbh
 */
public class UserResetCredentialsDTO implements DataTransferObject {

    private static final long serialVersionUID = 7100904135169446743L;
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
