package com.example.dath.eshop.models;

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
public class CartLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float quantity;

    @Column(name = "total_amount", nullable = false)
    private float totalAmount;

    @Column(name = "sub_total_amount", nullable = false)
    private float subTotalAmount;

    @Column(name = "tax_total_amount", nullable = false)
    private float taxTotalAmount;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Changed to PERSIST and MERGE
    @JoinColumn(name = "cart_id")
    private Cart cartId;

    @ManyToOne(cascade = CascadeType.MERGE) // Left as MERGE, considering you might not want to persist the product
    @JoinColumn(name = "product_id")
    private Product productId;

    public CartLineItem() {
        this.quantity = 0;
        this.totalAmount = 0;
        this.subTotalAmount = 0;
        this.taxTotalAmount = 0;
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date(); // Automatically set updatedAt when entity is updated
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CartLineItem{")
                .append("id=")
                .append(id)
                .append(", quantity=")
                .append(quantity)
                .append(", totalAmount=")
                .append(totalAmount)
                .append(", subTotalAmount=")
                .append(subTotalAmount)
                .append(", taxTotalAmount=")
                .append(taxTotalAmount)
                .append(", createdAt=")
                .append(createdAt)
                .append(", updatedAt=")
                .append(updatedAt)
                .append(", cartId=")
                .append(cartId)
                .append(", productId=")
                .append(productId)
                .append('}');
        return sb.toString();
    }
}
