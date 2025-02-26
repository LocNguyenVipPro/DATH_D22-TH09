package com.example.locnguyen.techzone.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Order;
import com.example.locnguyen.techzone.models.OrderLineItem;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Integer> {
    public Page<OrderLineItem> findByOrderId(Order orderId, Pageable pageable);

    @Query("SELECT p, SUM(oli.quantity) AS totalOrdered " + "FROM Product p "
            + "JOIN OrderLineItem oli ON p.id = oli.productId.id "
            + "WHERE p.isActive = true "
            + "GROUP BY p.id "
            + "ORDER BY totalOrdered DESC")
    List<Object[]> findProductsOrderedMost(Pageable pageable);
}
