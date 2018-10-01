package de.bogenliga.application.services.v1.user.model;

import java.util.List;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDTO {

    private int id;
    private String username;
    private String email;
    private List<UserRole> roles;


    public int getId() {
        return id;
    }


    public void setId(final int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(final String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


    public List<UserRole> getRoles() {
        return roles;
    }


    public void setRoles(final List<UserRole> roles) {
        this.roles = roles;
    }
}
