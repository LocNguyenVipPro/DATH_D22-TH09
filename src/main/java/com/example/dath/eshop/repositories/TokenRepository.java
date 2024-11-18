package com.example.dath.eshop.repositories;

import com.example.dath.eshop.models.Token;
import com.example.dath.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    Token findByUser(User user);

}
