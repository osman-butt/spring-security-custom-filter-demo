package ek.osnb.demo.security.apikey;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public final class ApiKeyConfigurer extends AbstractHttpConfigurer<ApiKeyConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http)  {
        ApiKeyAuthenticationProvider provider = http.getSharedObject(ApiKeyAuthenticationProvider.class);
        if (provider != null) {
            http.authenticationProvider(provider);
        }
    }

    @Override
    public void configure(HttpSecurity http) {
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        ApiKeyAuthFilter filter = context.getBean(ApiKeyAuthFilter.class);
        if (filter == null) {
            throw new IllegalStateException("ApiKeyAuthFilter bean not found");
        }
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    public static ApiKeyConfigurer apiKey() {
        return new ApiKeyConfigurer();
    }
}

