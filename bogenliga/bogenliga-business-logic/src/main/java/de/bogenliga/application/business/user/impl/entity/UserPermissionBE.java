package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserPermissionBE implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private long userId;
    private String permissionName;


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
