package com.example.repository;

import com.example.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);
}

