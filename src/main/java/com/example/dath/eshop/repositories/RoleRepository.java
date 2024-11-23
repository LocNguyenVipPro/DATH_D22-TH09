package com.example.dath.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dath.eshop.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
