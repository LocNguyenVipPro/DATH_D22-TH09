package com.example.dath.eshop.dto;

import com.example.dath.eshop.model.Order;
import com.example.dath.eshop.model.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;

    private Integer quantity;

    private Float totalAmount;

    private Float subTotalAmount;

    private Float taxTotalAmount;

    private Product product; // Đổi tên từ productId -> product

    private Order order; // Đổi tên từ orderId -> order
}
