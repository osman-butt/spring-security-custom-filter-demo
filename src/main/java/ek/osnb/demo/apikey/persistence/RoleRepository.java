package ek.osnb.demo.apikey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface RoleRepository extends JpaRepository<Role, String> {
}
