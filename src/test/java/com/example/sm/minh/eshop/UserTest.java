package com.example.sm.minh.eshop;

import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.repositories.RoleRepository;
import com.example.sm.minh.eshop.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository rolesRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void CteateNewUser()
    {
        User users=new User("admin","100","Ho","minh");
        users.setPassword(bCryptPasswordEncoder.encode("admin"));
//        Roles role=this.rolesRepository.findById(1).get();
//        users.setActive(true);
//        users.addRoles(role);
        User SavedUser=this.userRepository.save(users);
        Assertions.assertTrue(SavedUser.getId()!=0);
    }
    @Test
    public void CreateMoreUsers()
    {
        List<User>usersList=new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            User users=new User("hosyminh1182004@gmail.com"+i,"123"+i,"Ho"+i,"minh"+i);
            usersList.add(users);
        }
        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }

    @Test
    public void set()
    {
        User users=this.userRepository.findUsersByUserName("admin");
        System.out.println(users);
//        this.userRepository.save(users);
//        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }

    @Test
    public void SetActive()
    {
        User users=this.userRepository.findById(1).get();
        users.setActive(true);
        this.userRepository.save(users);
//        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }
}
