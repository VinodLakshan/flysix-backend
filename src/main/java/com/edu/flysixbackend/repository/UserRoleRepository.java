package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
