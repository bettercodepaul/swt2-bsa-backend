package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a composed and enriched business entity of the user and the role business entity.
 *
 * The user role is resolved with a JOIN via the user, user-roles and roles.
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserRoleExtBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private Long userId;
    private String userEmail;
    private Long roleId;
    private String roleName;


    /**
     * Constructor
     */
    public UserRoleExtBE() {
        // empty
    }


    @Override
    public String toString() {
        return "UserRoleExtBE{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
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

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

}
