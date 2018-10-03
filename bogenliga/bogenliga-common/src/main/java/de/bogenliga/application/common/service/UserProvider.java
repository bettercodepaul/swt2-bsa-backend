package de.bogenliga.application.common.service;

import java.security.Principal;

/**
 * I handle operations with the current user.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public final class UserProvider {

    /**
     * Constructor
     */
    private UserProvider() {
        // empty
    }


    /**
     * Extract the currently authenticated user id from the {@link Principal} object.
     *
     * @param principal with the authenticated user
     *
     * @return user id
     */
    public static long getCurrentUserId(final Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
