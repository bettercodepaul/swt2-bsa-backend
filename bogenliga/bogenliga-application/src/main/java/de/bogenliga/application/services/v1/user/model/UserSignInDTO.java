package de.bogenliga.application.services.v1.user.model;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserSignInDTO extends UserDTO implements DataTransferObject {

    private static final long serialVersionUID = 6021771923873844994L;

    private String jwt;
    private Set<UserPermission> permissions;
    private Long vereinId;
    private List<Integer> veranstaltungenIds;
    private List<Integer> wettkampfIds;

    public UserSignInDTO() {
        // empty constructor
    }


    public UserSignInDTO(final long id, final long version, final String email, final String jwt,
                         final Set<UserPermission> permissions) {
        super(id, version, email);
        this.jwt = jwt;
        this.permissions = permissions == null ? Collections.emptySet() : new HashSet<>(permissions);
    }


    /**
     * Copy constructor
     *
     * @param userDTO to copy
     */
    public UserSignInDTO(final UserDTO userDTO) {
        super(userDTO);
    }


    public String getJwt() {
        return jwt;
    }


    public void setJwt(final String jwt) {
        this.jwt = jwt;
    }


    public Set<UserPermission> getPermissions() {
        return permissions;
    }


    public void setPermissions(final Set<UserPermission> permissions) {
        this.permissions = permissions;
    }


    public Long getVereinId() {
        return vereinId;
    }


    public void setVereinId(Long vereinId) {
        this.vereinId = vereinId;
    }


    public List<Integer> getVeranstaltungenIds() {
        return veranstaltungenIds;
    }


    public void setVeranstaltungenIds(List<Integer> veranstaltungenIds) {
        this.veranstaltungenIds = veranstaltungenIds;
    }


    public List<Integer> getWettkampfIds() {
        return wettkampfIds;
    }


    public void setWettkampfIds(List<Integer> wettkampfIds) {
        this.wettkampfIds = wettkampfIds;
    }
}
