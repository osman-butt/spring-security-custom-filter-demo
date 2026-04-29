package ek.osnb.demo.apikey;

public interface ApiKeyManager {
    ApiKeyDetails findByToken(String token);
    CreateApiKeyResponse create(String name, String... roles);
    boolean existsByName(String name);
}
