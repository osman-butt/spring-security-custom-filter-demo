package ek.osnb.demo.apikey.inmemory;

import ek.osnb.demo.apikey.ApiKeyManager;
import ek.osnb.demo.apikey.CreateApiKeyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class SeedKeys implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SeedKeys.class);
    private final ApiKeyManager manager;

    SeedKeys(ApiKeyManager manager) {
        this.manager = manager;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Only if the manager is in-memory, to avoid creating keys in a real database during development.
        if (!(manager instanceof InMemoryApiKeyManager)) {
            return;
        }

        CreateApiKeyResponse adminKey = manager.create("admin key", "ADMIN", "USER");
        CreateApiKeyResponse userKey = manager.create("user key", "USER");
        log.warn("""
                    

                    Using generated API keys for development:
                    
                        - User API Key: {}
                        - Admin API Key: {}
                    
                    These keys are for development purposes only and should not be used in production.
                    """, userKey.token(), adminKey.token());
    }
}
