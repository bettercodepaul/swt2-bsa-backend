package de.bogenliga.application.business.user.api.types;

import de.bogenliga.application.common.component.types.ValueObject;

/**
 * Contains the values of the user business entity.
 */
public class UserVO implements ValueObject {
    private int id;
    private String email;
    private String salt;
    private String password;

    // TODO setter für ID, oder werden diese von DB zugewiesen?
    //TODO technical columns?

    public UserVO(){
        // empty constructor
    }

    public UserVO(int id, String email, String salt, String password) {
        this.id = id;
        this.email = email;
        this.salt = salt;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
