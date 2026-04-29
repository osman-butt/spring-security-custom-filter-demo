package ek.osnb.demo.security.apikey;

public record ApiKeyPrincipal(
        Long apiKeyId,
        String name,
        String publicId
) {}

