package logica.Clases;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordService {

    private static final String ALGORITHM =
            "PBKDF2WithHmacSHA256";

    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private PasswordService() {
    }

    public static String hash(char[] password) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria.");
        }

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] derivedHash = derive(
                password, salt, ITERATIONS);

        Base64.Encoder encoder = Base64.getEncoder();

        return "pbkdf2-sha256$" + ITERATIONS + "$"
                + encoder.encodeToString(salt) + "$"
                + encoder.encodeToString(derivedHash);
    }

    public static boolean verify(
            char[] password, String storedValue) {

        if (password == null || storedValue == null) {
            return false;
        }

        String[] parts = storedValue.split("\\$", -1);

        if (parts.length != 4
                || !parts[0].equals("pbkdf2-sha256")) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);

            if (iterations < ITERATIONS || iterations > 2_000_000) {
                return false;
            }

            Base64.Decoder decoder = Base64.getDecoder();

            byte[] salt = decoder.decode(parts[2]);
            byte[] expectedHash = decoder.decode(parts[3]);

            if (salt.length != SALT_LENGTH
                    || expectedHash.length != KEY_LENGTH / 8) {
                return false;
            }

            byte[] actualHash = derive(
                    password, salt, iterations);

            return MessageDigest.isEqual(
                    expectedHash, actualHash);

        } catch (IllegalArgumentException e) {
            // El formato, el número o el Base64 no son válidos.
            return false;
        }
    }

    private static byte[] derive(
            char[] password, byte[] salt, int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
                password, salt, iterations, KEY_LENGTH);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            return factory.generateSecret(spec).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "No se pudo calcular el hash de la contraseña.",
                    e);
        } finally {
            spec.clearPassword();
        }
    }
}
