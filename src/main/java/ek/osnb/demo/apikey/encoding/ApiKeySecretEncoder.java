package ek.osnb.demo.apikey.encoding;

public interface ApiKeySecretEncoder {
    String encode(String rawSecret);
    boolean matches(String rawSecret, String encodedSecret);
}
