package ek.osnb.demo.apikey.persistence;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String prefix;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private boolean active = true;

    protected ApiKey() {}

    static ApiKey create(String prefix, String name, String hash, Set<Role> roles) {
        ApiKey apiKey = new ApiKey();
        apiKey.prefix = prefix;
        apiKey.name = name;
        apiKey.hash = hash;
        apiKey.roles = roles;
        apiKey.active = true;
        return apiKey;
    }

    public Long getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getHash() {
        return hash;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public String getName() {
        return name;
    }
}
