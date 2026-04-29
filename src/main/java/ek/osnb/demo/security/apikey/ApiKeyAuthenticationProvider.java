package ek.osnb.demo.security.apikey;

import ek.osnb.demo.apikey.ApiKeyDetails;
import ek.osnb.demo.apikey.ApiKeyManager;
import ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
final class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationProvider.class);
    private final ApiKeyManager apiKeyManager;
    private final ApiKeySecretEncoder apiKeyEncoder;

    public ApiKeyAuthenticationProvider(ApiKeyManager apiKeyManager, ApiKeySecretEncoder apiKeyEncoder) {
        this.apiKeyManager = apiKeyManager;
        this.apiKeyEncoder = apiKeyEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String rawToken = (String) authentication.getCredentials();

        if (rawToken == null || rawToken.isBlank()) {
            log.warn("API key token was null or blank");
            throw new BadCredentialsException("Invalid API key");
        }

        ApiKeyDetails apiKey = apiKeyManager.findByToken(rawToken);
        String secretTokenPart = rawToken.split("\\.", 2)[1];

        if (!apiKeyEncoder.matches(secretTokenPart, apiKey.hash())) {
            log.warn("API key token hash did not match stored hash. id={}, name={}, publicId={}", apiKey.id(), apiKey.name(), apiKey.publicId());
            throw new BadCredentialsException("Invalid API key");
        }

        log.trace("API key matched with details. id={}, name={}, publicId={}", apiKey.id(), apiKey.name(), apiKey.publicId());

        List<SimpleGrantedAuthority> authorities = apiKey.roles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        ApiKeyPrincipal principal = new ApiKeyPrincipal(
                apiKey.id(),
                apiKey.name(),
                apiKey.publicId()
        );

        return ApiKeyAuthenticationToken.authenticated(principal, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
