package de.bogenliga.application.business.user.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a business entity :  user-role.
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserRoleBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private Long userId;
    private Long roleId;


    /**
     * Constructor
     */
    public UserRoleBE() {
        // empty
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }


    @Override
    public String toString() {
        return "UserRoleBE{" +
                "userId=" + userId +
                ", roleId='" + roleId + '\'' +
                '}';
    }


}
