package de.bogenliga.application.services.v1.user.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Der Name und ID der dem User zugeordneten Rolle werden gehalten
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public class UserRoleDTO implements DataTransferObject {

    private static final long serialVersionUID = -8591546299429551992L;
    private Long id;
    private String email;
    private Long roleId;
    private String roleName;
    private boolean active;
    private Long version;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;


    /**
     * Default constructor
     */
    public UserRoleDTO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */

    //Falls notwendig dekommentieren
    /**
    public UserRoleDTO(final Long id, final String email, final boolean active, final Long roleId, final String RoleName, final Long version, final String dsbMitgliedVorname, final String dsbMitgliedNachname) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.roleName = RoleName;
        this.version = version;
        this.active = active;
        this.dsbMitgliedVorname = dsbMitgliedVorname;
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }
     **/



    /**
     * Copy constructor
     *
     * @param userDTO to copy
     */

    //Falls notwenig dekommentieren
/**
    public UserRoleDTO(final UserRoleDTO userDTO) {
        this(userDTO.getId(), userDTO.getEmail(), userDTO.isActive(), userDTO.getRoleId(), userDTO.getRoleName(), userDTO.getVersion(),
                userDTO.getDsbMitgliedVorname(), userDTO.getDsbMitgliedNachname());
    }
 **/



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
    public void setRoleName(final String roleName) {this.roleName = roleName;}

    public Long getVersion() {
        return version;
    }
    public void setVersion(final Long version) {this.version = version;}

    public boolean isActive () {return this.active;}
    public void setActive (boolean active) {this.active = active;}

    public String getDsbMitgliedNachname() {return dsbMitgliedNachname;}
    public void setDsbMitgliedNachname(final String dsbMitgliedNachname) {this.dsbMitgliedNachname = dsbMitgliedNachname;}

    public String getDsbMitgliedVorname() {return dsbMitgliedVorname;}
    public void setDsbMitgliedVorname(final String dsbMitgliedVorname) {this.dsbMitgliedVorname = dsbMitgliedVorname;}
}
