package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

public class UserFailedSignInAttemptsBE implements BusinessEntity {
    private static final long serialVersionUID = -1560862619093862110L;

    private long userId;
    private int failedLoginAttempts;


    public UserFailedSignInAttemptsBE() {
        // empty constructor
    }


    @Override
    public String toString() {
        return "UserBE{" +
                "userId=" + userId +
                ", failedLoginAttempts='" + failedLoginAttempts + '\'' +
                '}';
    }


    public long getUserId() {
        return userId;
    }


    public void setUserId(final long userId) {
        this.userId = userId;
    }


    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }


    public void setFailedLoginAttempts(final int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }
}
