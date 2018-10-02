package de.bogenliga.application.business.user.impl.businessactivity;

import org.springframework.stereotype.Component;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class PasswordHashingBA {

    public String calculateHash(final String password, final String salt) {
        return password;
    }
}
