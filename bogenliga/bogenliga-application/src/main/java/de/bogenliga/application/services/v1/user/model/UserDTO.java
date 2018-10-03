package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private long id;
    private long version;
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
    public UserDTO(final long id, final long version, final String email) {
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


    public long getId() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


    public long getVersion() {
        return version;
    }


    public void setVersion(final long version) {
        this.version = version;
    }
}
