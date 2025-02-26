package com.example.locnguyen.techzone.dto;

import com.example.locnguyen.techzone.models.Order;
import com.example.locnguyen.techzone.models.Product;

public class OrderDTO {
    private Integer id;

    private Integer quantity;

    private Float totalAmount;

    private Float subTotalAmount;

    private Float taxTotalAmount;

    private Product productId;
    private Order orderId;

    public OrderDTO(
            Integer id,
            Integer quantity,
            Float totalAmount,
            Float subTotalAmount,
            Float taxTotalAmount,
            Product productId,
            Order orderId) {
        this.id = id;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.subTotalAmount = subTotalAmount;
        this.taxTotalAmount = taxTotalAmount;
        this.productId = productId;
        this.orderId = orderId;
    }
}
