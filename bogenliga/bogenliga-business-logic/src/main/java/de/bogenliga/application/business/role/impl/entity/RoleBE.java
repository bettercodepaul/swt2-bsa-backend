package de.bogenliga.application.business.role.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a composed business entity of the user and the permission business entity.
 *
 * The user permissions are resolved with a JOIN via the user roles, roles, role permissions and permissions.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class RoleBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private long roleId;
    private String roleName;


    /**
     * Constructor
     */
    public RoleBE() {
        // empty
    }

    @Override
    public String toString() {
        return "RoleBE{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }


    public long getRoleId() {
        return roleId;
    }


    public void setRoleId(final long roleId) {
        this.roleId = roleId;
    }


    public String getRoleName() {
        return roleName;
    }


    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

}
