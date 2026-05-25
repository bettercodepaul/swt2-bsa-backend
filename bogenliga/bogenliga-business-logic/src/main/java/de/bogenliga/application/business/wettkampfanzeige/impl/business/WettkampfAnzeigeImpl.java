package de.bogenliga.application.business.wettkampfanzeige.impl.business;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Mira Dietschmann, mira.dietschmann@student.reutlingen-university.de
 */
@Component
public class WettkampfAnzeigeImpl {
    private final SecureRandom random = new SecureRandom();

    public String generatePhysischeBildschirmId() {
        // Alpha-numeric characters
        final String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789";
        char[] characters = new char[4];
        for (int i = 0; i < characters.length; i++) {
            final int characterInt = random.nextInt(validCharacters.length());
            characters[i] = validCharacters.charAt(characterInt);
        }
        String id;
        id = new String(characters);
        return id;
    }
}
