package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

public class UserBE implements BusinessEntity {
    private int userId;
    private String userEmail;
    private String userSalt;
    private String userPassword;

    public UserBE(){
        // empty constructor
    }

    @Override
    public String toString() {
        return "UserBE{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", userSalt='" + userSalt + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
