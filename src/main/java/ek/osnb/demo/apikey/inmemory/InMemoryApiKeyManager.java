package ek.osnb.demo.apikey.inmemory;

import ek.osnb.demo.apikey.ApiKeyDetails;
import ek.osnb.demo.apikey.ApiKeyManager;
import ek.osnb.demo.apikey.ApiKeyNotFoundException;
import ek.osnb.demo.apikey.CreateApiKeyResponse;
import ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder;
import ek.osnb.demo.apikey.generate.ApiKeyGenerator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


class InMemoryApiKeyManager implements ApiKeyManager {
    private final AtomicLong idGen = new AtomicLong(1L);
    private final Map<String, ApiKeyDetails> apiKeys = new ConcurrentHashMap<>();

    private final ApiKeyGenerator apiKeyGenerator;
    private final ApiKeySecretEncoder encoder;

    public InMemoryApiKeyManager(ApiKeyGenerator apiKeyGenerator, ApiKeySecretEncoder encoder) {
        this.apiKeyGenerator = apiKeyGenerator;
        this.encoder = encoder;
    }

    @Override
    public ApiKeyDetails findByToken(String token) {
        var extractedToken = extractToken(token);
        String publicId = extractedToken.publicId();

        ApiKeyDetails apiKeyDetails = apiKeys.get(publicId);
        if (apiKeyDetails == null) {
            throw new ApiKeyNotFoundException("API key not found: " + publicId);
        }

        return new ApiKeyDetails(
                apiKeyDetails.id(),
                apiKeyDetails.name(),
                apiKeyDetails.publicId(),
                apiKeyDetails.hash(),
                apiKeyDetails.roles()
        );
    }

    @Override
    public CreateApiKeyResponse create(String name, String... roles) {
        var key = apiKeyGenerator.generate();
        long id = idGen.getAndIncrement();

        apiKeys.put(key.prefix(), new ApiKeyDetails(id, name, key.prefix(), encoder.encode(key.secret()), List.of(roles)));
        return new CreateApiKeyResponse(
                id,
                name,
                key.token()
        );
    }

    @Override
    public boolean existsByName(String name) {
        return apiKeys.values().stream()
                .anyMatch(key -> key.name().equals(name));
    }

    private record ExtractedToken(String publicId, String tokenSecret) {
    }

    private ExtractedToken extractToken(String token) {
        int dot = token.indexOf(".");
        if (dot < 1) {
            throw new ApiKeyNotFoundException("Invalid API key format");
        }

        return new ExtractedToken(
                token.substring(0, dot),
                token.substring(dot + 1)
        );
    }
}
