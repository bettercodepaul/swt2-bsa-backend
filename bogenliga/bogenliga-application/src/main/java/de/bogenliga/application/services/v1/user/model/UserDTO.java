package de.bogenliga.application.services.v1.user.model;

import java.util.Set;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDTO {

    private int id;
    private int version;
    private String email;
    private Set<UserPermission> permissions;


    public int getId() {
        return id;
    }


    public void setId(final int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


    public Set<UserPermission> getPermissions() {
        return permissions;
    }


    public void setPermissions(final Set<UserPermission> permissions) {
        this.permissions = permissions;
    }
}
