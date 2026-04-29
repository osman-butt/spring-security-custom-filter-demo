package ek.osnb.demo.apikey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record ApiKeyDetails(Long id, String name, String publicId, @JsonIgnore String hash, List<String> roles) {
    @Override
    public String toString() {
        return "ApiKeyDetails[id=" + id + ", name=" + name + ", publicId=" + publicId + ", roles=" + roles + "]";
    }
}
