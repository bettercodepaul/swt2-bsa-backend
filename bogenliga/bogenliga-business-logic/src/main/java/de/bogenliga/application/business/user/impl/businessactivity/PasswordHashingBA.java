package de.bogenliga.application.business.user.impl.businessactivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.stereotype.Component;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 *
 * @see <a href="https://crackstation.net/hashing-security.htm">Salted Password Hashing - Doing it Right</a>
 */
@Component
public class PasswordHashingBA {


    private static final String SHA_512 = "SHA-512";


    private String convertBytesToHex(byte[] bytes) {
        String generatedPassword;
        final StringBuilder sb = new StringBuilder();
        for (final byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
        return generatedPassword;
    }


    /**
     * Generate a hashed password.
     *
     * @param password plaintext password
     * @param salt     user specific salt
     *
     * @return sha-512 hash value
     */
    String calculateHash(final String password, final String salt) {

        try {
            final MessageDigest md = MessageDigest.getInstance(SHA_512);
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            final byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return convertBytesToHex(bytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new TechnicalException(ErrorCode.UNEXPECTED_ERROR, e);
        }
    }


    /**
     * Generate a salt fo a new user.
     * <p>
     * Use a random and a unique part.
     *
     * @return sha-512 hash value
     */
    String generateSalt() {
        try {
            final SecureRandom random = new SecureRandom();
            final byte[] randomSeed = new byte[100];
            random.nextBytes(randomSeed);

            byte[] uniqueSeed = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);

            final MessageDigest md = MessageDigest.getInstance(SHA_512);
            md.update(randomSeed);
            final byte[] bytes = md.digest(uniqueSeed);

            return convertBytesToHex(bytes);

        } catch (final NoSuchAlgorithmException e) {
            throw new TechnicalException(ErrorCode.UNEXPECTED_ERROR, e);
        }
    }
}
