package com.example.sm.minh.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sm.minh.eshop.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
