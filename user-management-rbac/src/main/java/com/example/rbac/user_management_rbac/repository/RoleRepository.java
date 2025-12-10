package com.example.rbac.user_management_rbac.repository;

import com.example.rbac.user_management_rbac.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
}
