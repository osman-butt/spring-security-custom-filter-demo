package ek.osnb.demo.apikey.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    @Query("""
                select k from ApiKey k
                left join fetch k.roles
                where k.prefix = :prefix and k.active = true
            """)
    Optional<ApiKey> findByPrefixAndActiveTrueWithRoles(String prefix);

    boolean existsByName(String name);
}
