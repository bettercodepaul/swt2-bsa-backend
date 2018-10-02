package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class UserBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private long userId;
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


    public long getUserId() {
        return userId;
    }


    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserSalt() {
        return userSalt;
    }


    public void setUserSalt(final String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserPassword() {
        return userPassword;
    }


    public void setUserPassword(final String userPassword) {
        this.userPassword = userPassword;
    }
}
