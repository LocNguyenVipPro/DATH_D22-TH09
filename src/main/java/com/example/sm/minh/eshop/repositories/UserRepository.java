package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findUsersByUserName(String userName);

}
