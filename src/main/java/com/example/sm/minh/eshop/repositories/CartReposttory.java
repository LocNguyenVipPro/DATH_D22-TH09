package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Cart;
import com.example.sm.minh.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartReposttory extends JpaRepository<Cart,Integer> {
    public Cart findByUserId(Integer user_id);

}