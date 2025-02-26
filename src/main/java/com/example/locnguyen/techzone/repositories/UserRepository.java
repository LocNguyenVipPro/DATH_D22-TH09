package com.example.locnguyen.techzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findUsersByUserName(String userName);
}
