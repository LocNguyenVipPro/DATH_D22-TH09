package com.example.locnguyen.techzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
