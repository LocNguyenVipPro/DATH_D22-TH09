package com.example.sm.minh.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sm.minh.eshop.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findUsersByUserName(String userName);
}
