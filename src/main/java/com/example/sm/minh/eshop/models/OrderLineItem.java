package com.example.sm.minh.eshop.models;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    private Float totalAmount;

    private Float subTotalAmount;

    private Float taxTotalAmount;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product productId;

    @Override
    public String toString() {
        return "OrderLineItem{" + "id="
                + id + ", quantity="
                + quantity + ", totalAmount="
                + totalAmount + ", subTotalAmount="
                + subTotalAmount + ", taxTotalAmount="
                + taxTotalAmount + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + '}';
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order orderId;

    public OrderLineItem() {

        this.quantity = 0;
        this.totalAmount = 0f;
        this.subTotalAmount = 0f;
        this.taxTotalAmount = 0f;
        this.createdAt = new Date();
    }
}
