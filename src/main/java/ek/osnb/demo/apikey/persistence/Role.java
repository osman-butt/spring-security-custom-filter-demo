package ek.osnb.demo.apikey.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class Role {
    @Id
    private String name;

    protected Role() {
    }

    public static Role create(String name) {
        Role role = new Role();
        role.name = name;
        return role;
    }

    public String getName() {
        return name;
    }
}
