package com.example.sm.minh.eshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sm.minh.eshop.models.Order;
import com.example.sm.minh.eshop.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    public Order findByUserId(User userId);
}
