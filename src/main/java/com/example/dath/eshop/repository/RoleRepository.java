package com.example.dath.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dath.eshop.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
