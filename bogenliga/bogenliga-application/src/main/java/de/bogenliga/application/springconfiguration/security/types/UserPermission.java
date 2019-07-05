package de.bogenliga.application.springconfiguration.security.types;

import org.springframework.security.core.GrantedAuthority;

/**
 * I contain the permissions of an user.
 * <p>
 * The permissions are configured in the database and stored in the JSON Web Token after the signIn.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum UserPermission implements GrantedAuthority {

    /*
     * Permissions to work with:
     * DSB_Mitglied
     * Verein
     * Region
     * Liga
     */
    CAN_READ_STAMMDATEN,
    CAN_MODIFY_STAMMDATEN,

    /*
     * Permissions to work with:
     * Wettkampf
     * Match
     * Passe
     * Kampfrichter
     */
    CAN_READ_WETTKAMPF,
    CAN_MODIFY_WETTKAMPF,

    /*
     * Permissions to work with:
     * Veranstaltung
     * Wettkampftyp
     * Ligatabelle
     * Klasse
     * Disziplin
     */
    CAN_READ_SPORTJAHR,
    CAN_MODIFY_SPORTJAHR,

    /**
     * Permissions to work with: Benutzer Rolle Recht Configuration
     * Lizenz
     */
    CAN_READ_SYSTEMDATEN,
    CAN_MODIFY_SYSTEMDATEN,
    ;


    private static final String PREFIX = "ROLE_";


    /**
     * Get the enum of a given value.
     *
     * @param value string representation of the permission
     *
     * @return enum of the value or null if not match is found
     */
    public static UserPermission fromValue(final String value) {
        for (final UserPermission userPermission : UserPermission.values()) {
            final String permissionWithPrefix = PREFIX + userPermission.name();
            final String permissionWithoutPrefix = userPermission.name();

            if (permissionWithPrefix.equalsIgnoreCase(value)
                    || permissionWithoutPrefix.equalsIgnoreCase(value)) {
                return userPermission;
            }
        }
        return null;
    }


    @Override
    public String getAuthority() {
        return PREFIX + name();
    }

}
