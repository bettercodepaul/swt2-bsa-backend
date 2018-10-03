package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * I´m a composed representation of the user and the user sign in history.
 * <p>
 * The number of failed sign in attempts is calculated with a SQL query.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserFailedSignInAttemptsBE implements BusinessEntity {
    private static final long serialVersionUID = -1560862619093862110L;

    private long userId;
    private int failedLoginAttempts;


    /**
     * Constructor
     */
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
