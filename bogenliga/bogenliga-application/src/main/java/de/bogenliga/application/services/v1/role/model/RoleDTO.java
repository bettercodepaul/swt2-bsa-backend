package de.bogenliga.application.services.v1.role.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class RoleDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
    private String email;
    private Long roleId;
    private String roleName;

    private Long version;


    /**
     * Default constructor
     */
    public RoleDTO() {
        // empty constructor
    }

    /**
     * Constructor with mandatory parameters
     */
    public RoleDTO(final Long id, final Long version, final String roleName) {
        this.id = id;
        this.version = version;
        this.roleName = roleName;
    }


    /**
     * Constructor with mandatory parameters
     */
    public RoleDTO(final Long id, final String email, final Long roleId, final String RoleName, final Long version) {
        this.id = id;
        this.email = email;
        this.roleId = id;
        this.roleName = email;
        this.version = version;
    }



    /**
     * Copy constructor
     *
     * @param userDTO to copy
     */
    public RoleDTO(final RoleDTO userDTO) {
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
