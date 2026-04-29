package ek.osnb.demo.apikey.generate;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApiKeyGeneratorConfig {
    @Bean
    @ConditionalOnMissingBean(ApiKeyGenerator.class)
    ApiKeyGenerator apiKeyGenerator() {
        return new DefaultApiKeyGenerator();
    }
}
