package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Token;
import com.example.sm.minh.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    Token findByUser(User user);

}
