package com.example.sm.minh.eshop.controllers;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sm.minh.eshop.models.Role;
import com.example.sm.minh.eshop.repositories.RoleRepository;

@Component
public class RoleInitializer {
    @Autowired
    private final RoleRepository rolesRepository;

    @Autowired
    public RoleInitializer(RoleRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        createRoleIfNotExists(1, "Admin", "Do Anything");
        createRoleIfNotExists(2, "User", "User role is for those who engage in buying and selling goods");
    }

    private void createRoleIfNotExists(int roleId, String roleName, String roleDescription) {
        if (!rolesRepository.existsById(roleId)) {
            Role role = new Role(roleName, roleDescription);
            role.setId(roleId);
            rolesRepository.save(role);
            System.out.println("Created role: " + roleName);
        } else {
            System.out.println("Role with ID " + roleId + " already exists.");
        }
    }
}
