package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

class TokenTest {


    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void testToken() {

        String publicId = UUID.randomUUID().toString();
        String tokenPart = UUID.randomUUID().toString();
        String token = publicId + "." + tokenPart;
        String hash = encoder.encode(tokenPart);
        System.out.println("Public ID: " + publicId);
        System.out.println("Token: " + token);
        System.out.println("Hash: " + hash);

        System.out.println(UUID.randomUUID().toString());
    }


    @Test
    void validate() {
        String lookupHash = "$2a$10$XcKJVkEzFarP6cl32VliK.H2mNc7WZZT0QQ7xxSUAFPFIdFIgPu7.";
        String token = "lodwdcyKROYlSuYh.FS0lnqOODk3s6yLrzp7fab8yhIIdrIuytMDa_7N5yAk";

        String extractedTokenPart = token.substring(token.indexOf(".") + 1);
        boolean matches = encoder.matches(extractedTokenPart, lookupHash);
        System.out.println("Extracted token part: " + extractedTokenPart);
        System.out.println("Matches: " + matches);
    }
}
