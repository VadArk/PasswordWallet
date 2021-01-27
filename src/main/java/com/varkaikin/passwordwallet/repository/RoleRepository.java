package com.varkaikin.passwordwallet.repository;

import com.varkaikin.passwordwallet.model.ERole;
import com.varkaikin.passwordwallet.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
