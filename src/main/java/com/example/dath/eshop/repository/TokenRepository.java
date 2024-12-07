package com.example.dath.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dath.eshop.model.Token;
import com.example.dath.eshop.model.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByUser(User user);
}
