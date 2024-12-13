package com.example.sm.minh.eshop.dto;

import com.example.sm.minh.eshop.models.Order;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public class OrderDTO {
    private Integer id;

    private Integer quantity ;

    private Float totalAmount;

    private Float subTotalAmount;

    private Float taxTotalAmount;

    private Product productId;
    private Order orderId;

    public OrderDTO(Integer id, Integer quantity, Float totalAmount, Float subTotalAmount, Float taxTotalAmount, Product productId, Order orderId) {
        this.id = id;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.subTotalAmount = subTotalAmount;
        this.taxTotalAmount = taxTotalAmount;
        this.productId = productId;
        this.orderId = orderId;
    }
}
