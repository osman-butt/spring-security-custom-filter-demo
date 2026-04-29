package ek.osnb.demo.apikey.persistence;


import ek.osnb.demo.apikey.ApiKeyDetails;
import ek.osnb.demo.apikey.ApiKeyManager;
import ek.osnb.demo.apikey.ApiKeyNotFoundException;
import ek.osnb.demo.apikey.CreateApiKeyResponse;
import ek.osnb.demo.apikey.encoding.ApiKeySecretEncoder;
import ek.osnb.demo.apikey.generate.ApiKeyGenerator;
import ek.osnb.demo.apikey.generate.GeneratedApiKey;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
class JpaApiKeyManager implements ApiKeyManager {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeySecretEncoder encoder;
    private final ApiKeyGenerator apiKeyGenerator;
    private final RoleRepository roleRepository;

    JpaApiKeyManager(ApiKeyRepository apiKeyRepository, ApiKeySecretEncoder encoder, ApiKeyGenerator apiKeyGenerator, RoleRepository roleRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.encoder = encoder;
        this.apiKeyGenerator = apiKeyGenerator;
        this.roleRepository = roleRepository;
    }


    public ApiKeyDetails findByToken(String token) {
        var extractedToken = extractToken(token);
        String publicId = extractedToken.publicId();

        ApiKey apiKey = apiKeyRepository.findByPrefixAndActiveTrueWithRoles(publicId)
                .orElseThrow(() -> new ApiKeyNotFoundException("API key not found: " + publicId));

        return new ApiKeyDetails(
                apiKey.getId(),
                apiKey.getName(),
                apiKey.getPrefix(),
                apiKey.getHash(),
                apiKey.getRoles().stream().map(Role::getName).toList()
        );
    }

    @Transactional
    Role existElseCreateRole(String roleName) {
        return roleRepository.findById(roleName).orElseGet(() -> {
            try {
                Role newRole = Role.create(roleName);
                return roleRepository.saveAndFlush(newRole);
            } catch (DataIntegrityViolationException e) {
                // Race condition catch
                return roleRepository.findById(roleName)
                        .orElseThrow(() -> new IllegalStateException("Role not found after conflict: " + roleName));
            }
        });
    }

    @Override
    public CreateApiKeyResponse create(String name, String... roles) {
        if (roles.length == 0) {
            throw new IllegalArgumentException("At least one role must be specified");
        }

        Set<Role> roleEntities = Set.of(roles).stream()
                .map(this::existElseCreateRole)
                .collect(Collectors.toSet());

        GeneratedApiKey generated = apiKeyGenerator.generate();
        ApiKey apiKey = ApiKey.create(
                generated.prefix(),
                name,
                encoder.encode(generated.secret()),
                roleEntities
        );
        apiKeyRepository.save(apiKey);

        return new CreateApiKeyResponse(
                apiKey.getId(),
                apiKey.getName(),
                generated.token()
        );
    }

    @Override
    public boolean existsByName(String name) {
        return apiKeyRepository.existsByName(name);
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
