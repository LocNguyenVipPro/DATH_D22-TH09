package com.example.dath.eshop.repositories;

import com.example.dath.eshop.models.Order;
import com.example.dath.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    public Order findByUserId(User userId);

}
