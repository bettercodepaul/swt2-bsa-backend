package de.bogenliga.application.business.user.impl.entity;

import org.jboss.aerogear.security.otp.api.Base32;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the user business entity.
 * <p>
 * An user is a registered member of the application. The user can sign in into the system and use the functionality.
 * <p>
 * The user is a technical construct and related to the DSB member.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see CommonBusinessEntity
 */
public class UserBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long userId;
    private String userEmail;
    private String userSalt;
    private String userPassword;
    private Long dsbMitgliedId;
    private boolean using2FA = true;
    private boolean active = true;


    private String secret;


    public UserBE() {
        this.secret = Base32.random();
    }


    @Override
    public String toString() {
        return "UserBE{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", userSalt='" + userSalt + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", dsbMitgliedId='" + dsbMitgliedId + '\'' +
                '}';
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(final Long userId) {
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


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(final Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public boolean isUsing2FA() {
        return using2FA;
    }


    public void setUsing2FA(boolean using2FA) {
        this.using2FA = using2FA;
    }


    public String getSecret() {
        return secret;
    }


    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isActive() {return active;}

    public void setActive(boolean isActive) {this.active = isActive;}
}
