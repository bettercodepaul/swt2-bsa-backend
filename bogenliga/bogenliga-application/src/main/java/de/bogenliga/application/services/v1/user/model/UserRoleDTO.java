package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Der Name und ID der dem User zugeordneten Rolle werden gehalten
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserRoleDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
    private String email;
    private Long roleId;
    private String roleName;

    private Long version;


    /**
     * Default constructor
     */
    public UserRoleDTO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */
    public UserRoleDTO(final Long id, final String email, final Long roleId, final String RoleName, final Long version) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.roleName = RoleName;
        this.version = version;
    }



    /**
     * Copy constructor
     *
     * @param userDTO to copy
     */
    public UserRoleDTO(final UserRoleDTO userDTO) {
        this(userDTO.getId(), userDTO.getEmail(), userDTO.getRoleId(), userDTO.getRoleName(), userDTO.getVersion());
    }


    public Long getId() {
        return id;
    }
    public void setId(final Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(final String email) {
        this.email = email;
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

    public Long getVersion() {
        return version;
    }
    public void setVersion(final Long version) {
        this.version = version;
    }
}
