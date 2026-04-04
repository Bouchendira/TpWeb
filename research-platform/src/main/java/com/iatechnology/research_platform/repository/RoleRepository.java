package com.iatechnology.research_platform.repository;

import com.iatechnology.research_platform.entity.ERole;
import com.iatechnology.research_platform.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
