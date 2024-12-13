package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Order;
import com.example.sm.minh.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    public Order findByUserId(User userId);

}
