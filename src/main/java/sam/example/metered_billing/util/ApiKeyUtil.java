package sam.example.metered_billing.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

public class ApiKeyUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String generateAPIKey() {
        byte[] bytes = new byte[16]; // 16 bytes = 128 bits
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    public static String hashAPIKey(String apiKey) {
        return DigestUtils.sha256Hex(apiKey);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
