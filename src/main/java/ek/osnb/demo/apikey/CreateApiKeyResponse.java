package ek.osnb.demo.apikey;

public record CreateApiKeyResponse(
        Long id,
        String name,
        String token
) {
}
