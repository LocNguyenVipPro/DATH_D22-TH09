package com.example.dath.eshop.repositories;

import com.example.dath.eshop.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartReposttory extends JpaRepository<Cart,Integer> {
    public Cart findByUserId(Integer user_id);

}