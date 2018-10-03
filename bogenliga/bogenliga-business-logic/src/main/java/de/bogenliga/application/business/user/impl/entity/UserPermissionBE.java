package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * I´m a composed business entity of the user and the permission business entity.
 *
 * The user permissions are resolved with a JOIN via the user roles, roles, role permissions and permissions.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserPermissionBE implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private long userId;
    private String permissionName;


    /**
     * Constructor
     */
    public UserPermissionBE() {
        // empty
    }

    public long getUserId() {
        return userId;
    }


    public void setUserId(final long userId) {
        this.userId = userId;
    }


    public String getPermissionName() {
        return permissionName;
    }


    public void setPermissionName(final String permissionName) {
        this.permissionName = permissionName;
    }

}
