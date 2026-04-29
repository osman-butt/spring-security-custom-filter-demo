package ek.osnb.demo.security.apikey;

import ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder},
 * that adapts a {@link PasswordEncoder} to be used for encoding API key secrets.
 *
 *
 * It decouples spring security from the API key management logic, allowing for flexibility in choosing the encoding strategy.
 */
@Component
final class PasswordApiKeySecretEncoder implements ApiKeySecretEncoder {
    private final PasswordEncoder delegate;

    PasswordApiKeySecretEncoder(PasswordEncoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public String encode(String rawSecret) {
        return delegate.encode(rawSecret);
    }

    @Override
    public boolean matches(String rawSecret, String encodedSecret) {
        return delegate.matches(rawSecret, encodedSecret);
    }
}
