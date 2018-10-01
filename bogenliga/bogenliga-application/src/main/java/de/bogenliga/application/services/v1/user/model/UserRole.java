package de.bogenliga.application.services.v1.user.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

public enum UserRole implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;


    public static UserRole fromValue(final String value) {
        for (final UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(value.toUpperCase())) {
                return userRole;
            }
        }
        return null;
    }


    @Override
    public String getAuthority() {
        return name();
    }

}
