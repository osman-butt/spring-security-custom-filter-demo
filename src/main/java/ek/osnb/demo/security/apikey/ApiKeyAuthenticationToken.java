package ek.osnb.demo.security.apikey;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

final class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private String token;

    private ApiKeyAuthenticationToken(
            Object principal,
            String token,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    public static ApiKeyAuthenticationToken unauthenticated(String token) {
        return new ApiKeyAuthenticationToken(null, token, null);
    }

    public static ApiKeyAuthenticationToken authenticated(
            ApiKeyPrincipal principal,
            Collection<? extends GrantedAuthority> authorities
    ) {
        ApiKeyAuthenticationToken result =
                new ApiKeyAuthenticationToken(principal, null, authorities);
        result.setAuthenticated(true);
        return result;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
