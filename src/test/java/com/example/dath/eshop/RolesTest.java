package com.example.dath.eshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dath.eshop.model.Role;
import com.example.dath.eshop.repository.RoleRepository;

@SpringBootTest
public class RolesTest {
    @Autowired
    private RoleRepository rolesRepository;

    @Test
    public void createTwoRoles() {
        Role rolesAdmin = new Role("Admin", "Do AnyThing");
        Role rolesUser = new Role("User", "User role is for those who engage in buying and selling goods");
        Role rolesAdminSaved = this.rolesRepository.save(rolesAdmin);
        Role rolesUserSaved = this.rolesRepository.save(rolesUser);
        Assertions.assertTrue(rolesUserSaved.getId() != null && rolesAdminSaved.getId() != null);
    }
}
