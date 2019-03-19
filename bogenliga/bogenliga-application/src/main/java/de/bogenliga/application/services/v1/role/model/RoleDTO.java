package de.bogenliga.application.services.v1.role.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *  I map and construct {@link RoleDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class RoleDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
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
    public RoleDTO(final Long id, final String roleName, final Long version) {
        this.id = id;
        this.roleName = roleName;
        this.version = version;
    }



    /**
     * Copy constructor
     *
     * @param roleDTO to copy
     */
    public RoleDTO(final RoleDTO roleDTO) {
        this(roleDTO.getId(), roleDTO.getRoleName(), roleDTO.getVersion());
    }


    public Long getId() {
        return id;
    }
    public void setId(final Long id) {
        this.id = id;
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
