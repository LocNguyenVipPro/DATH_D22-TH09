package com.example.locnguyen.techzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Order;
import com.example.locnguyen.techzone.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    public Order findByUserId(User userId);
}
