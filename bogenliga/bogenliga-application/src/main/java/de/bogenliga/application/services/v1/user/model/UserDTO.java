package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
    private Long version;
    private String email;


    /**
     * Default constructor
     */
    public UserDTO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */
    public UserDTO(final Long id, final Long version, final String email) {
        this.id = id;
        this.version = version;
        this.email = email;
    }


    /**
     * Copy constructor
     *
     * @param userDTO to copy
     */
    public UserDTO(final UserDTO userDTO) {
        this(userDTO.getId(), userDTO.getVersion(), userDTO.getEmail());
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


    public Long getVersion() {
        return version;
    }


    public void setVersion(final Long version) {
        this.version = version;
    }
}
