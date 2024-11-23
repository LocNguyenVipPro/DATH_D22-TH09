package com.example.dath.eshop.dto;

import com.example.dath.eshop.models.Order;
import com.example.dath.eshop.models.Product;

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
