package com.example.locnguyen.techzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Token;
import com.example.locnguyen.techzone.models.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByUser(User user);
}
