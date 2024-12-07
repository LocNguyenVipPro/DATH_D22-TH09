package com.example.dath.eshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dath.eshop.model.Order;
import com.example.dath.eshop.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByUserId(User userId);
}
