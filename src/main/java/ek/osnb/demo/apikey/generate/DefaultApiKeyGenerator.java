package ek.osnb.demo.apikey.generate;

import java.security.SecureRandom;
import java.util.Base64;

final class DefaultApiKeyGenerator implements ApiKeyGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER =
            Base64.getUrlEncoder().withoutPadding();

    @Override
    public GeneratedApiKey generate() {
        String prefix = random(12);
        String secret = random(32);

        return new GeneratedApiKey(prefix, secret, prefix + "." + secret);
    }

    private String random(int bytes) {
        byte[] bytesArray = new byte[bytes];
        RANDOM.nextBytes(bytesArray);
        return ENCODER.encodeToString(bytesArray);
    }
}
