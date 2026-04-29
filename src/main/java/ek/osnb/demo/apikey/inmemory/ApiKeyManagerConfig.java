package ek.osnb.demo.apikey.inmemory;

import ek.osnb.demo.apikey.ApiKeyManager;
import ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder;
import ek.osnb.demo.apikey.generate.ApiKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApiKeyManagerConfig {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyManagerConfig.class);

    @Bean
    @ConditionalOnMissingBean(ApiKeyManager.class)
    ApiKeyManager apiKeyManager(
            ApiKeyGenerator apiKeyGenerator,
            ApiKeySecretEncoder passwordEncoder
    ) {
        log.warn("Using in-memory API key manager. This is not suitable for production use!");
        return new InMemoryApiKeyManager(apiKeyGenerator, passwordEncoder);
    }
}
