package com.example.dath.eshop;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.dath.eshop.model.User;
import com.example.dath.eshop.repository.RoleRepository;
import com.example.dath.eshop.repository.UserRepository;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void CreateNewUser() {
        User users = new User("admin", "100", "Ho", "minh");
        users.setPassword(bCryptPasswordEncoder.encode("admin"));
        //        Roles role=this.rolesRepository.findById(1).get();
        //        users.setActive(true);
        //        users.addRoles(role);
        User SavedUser = this.userRepository.save(users);
        Assertions.assertTrue(SavedUser.getId() != 0);
    }

    @Test
    public void CreateMoreUsers() {
        List<User> usersList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User users = new User("hosyminh1182004@gmail.com" + i, "123" + i, "Ho" + i, "minh" + i);
            usersList.add(users);
        }
        Assertions.assertTrue(this.userRepository.saveAll(usersList).size() != 0);
    }

    @Test
    public void set() {
        User users = this.userRepository.findUsersByUserName("admin");
        System.out.println(users);
        //        this.userRepository.save(users);
        //        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }

    @Test
    public void SetActive() {
        User users = this.userRepository.findById(1).get();
        users.setActive(true);
        this.userRepository.save(users);
        //        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }
}
