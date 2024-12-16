package com.example.sm.minh.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sm.minh.eshop.models.Cart;

@Repository
public interface CartReposttory extends JpaRepository<Cart, Integer> {
    public Cart findByUserId(Integer user_id);
}
