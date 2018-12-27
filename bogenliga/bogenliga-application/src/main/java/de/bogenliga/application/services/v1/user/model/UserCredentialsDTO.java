package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserCredentialsDTO implements DataTransferObject {

    private static final long serialVersionUID = 7100904135169446743L;
    private String username;
    private String password;


    public String getUsername() {
        return username;
    }
    public void setUsername(final String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(final String password) {
        this.password = password;
    }
}
