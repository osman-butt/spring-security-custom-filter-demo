package ek.osnb.demo.apikey.generate;

public record GeneratedApiKey(
        String prefix,
        String secret,
        String token
) {}
