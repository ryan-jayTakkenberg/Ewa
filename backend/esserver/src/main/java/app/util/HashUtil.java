package app.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String hash(String input) {
        if (input == null) {
            return "";
        }
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");

            // Update input string in message digest
            sha256Digest.update(input.getBytes());

            // Generate the hash
            byte[] hash = sha256Digest.digest();

            // Convert the byte array to a hexadecimal representation
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexStringBuilder.append('0');
                }
                hexStringBuilder.append(hex);
            }

            // Return the result
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }

}
