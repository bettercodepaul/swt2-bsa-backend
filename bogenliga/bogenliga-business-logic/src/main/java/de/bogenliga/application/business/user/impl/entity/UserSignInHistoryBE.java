package de.bogenliga.application.business.user.impl.entity;

import java.sql.Timestamp;
import de.bogenliga.application.business.user.impl.types.SignInResult;
import de.bogenliga.application.common.component.entity.BusinessEntity;


/**
 * I represent the user sign in history business entity.
 * <p>
 * The sign in history documents each sign in attempt of an user.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserSignInHistoryBE implements BusinessEntity {

    private static final long serialVersionUID = -3350215655826737938L;
    private long signInUserId;
    private SignInResult signInResult;
    private Timestamp signInAtUtc;


    /**
     * Constructor
     */
    public UserSignInHistoryBE() {
        // empty constructor
    }


    @Override
    public String toString() {
        return "UserBE{" +
                "signInUserId=" + signInUserId +
                ", signInResult='" + signInResult + '\'' +
                ", signInAtUtc='" + signInAtUtc + '\'' +
                '}';
    }


    public long getSignInUserId() {
        return signInUserId;
    }


    public void setSignInUserId(final long signInUserId) {
        this.signInUserId = signInUserId;
    }


    public SignInResult getSignInResult() {
        return signInResult;
    }


    public void setSignInResult(final SignInResult signInResult) {
        this.signInResult = signInResult;
    }


    public Timestamp getSignInAtUtc() {
        return signInAtUtc;
    }


    public void setSignInAtUtc(final Timestamp signInAtUtc) {
        this.signInAtUtc = signInAtUtc;
    }
}
