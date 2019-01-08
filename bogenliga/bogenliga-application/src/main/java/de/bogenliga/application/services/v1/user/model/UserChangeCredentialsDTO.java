package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserChangeCredentialsDTO implements DataTransferObject {

    private static final long serialVersionUID = 7100904135169446743L;
    private String password;
    private String newpassword;


    public String getPassword() {
        return password;
    }
    public void setPassword(final String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newpassword;
    }
    public void setNewPassword(final String newpassword) {
        this.newpassword = newpassword;
    }

}
