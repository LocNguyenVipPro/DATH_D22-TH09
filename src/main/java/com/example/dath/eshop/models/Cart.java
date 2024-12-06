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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float taxAmount;

    private float totalAmount; // Changed to 'float' for consistency

    private float countItem;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "is_active")
    private Boolean isActive = true; // Set default value for isActive

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Updated cascade type
    @JoinColumn(name = "users_id")
    private User user;

    public Cart() {
        this.countItem = 0;
        this.createdAt = new Date();
        this.taxAmount = 0;
        this.totalAmount = 0f;
        this.isActive = true; // Default value for isActive
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date(); // Automatically set updatedAt when entity is updated
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{")
                .append("id=")
                .append(id)
                .append(", taxAmount=")
                .append(taxAmount)
                .append(", totalAmount=")
                .append(totalAmount)
                .append(", countItem=")
                .append(countItem)
                .append(", updatedAt=")
                .append(updatedAt)
                .append(", deletedAt=")
                .append(deletedAt)
                .append(", createdAt=")
                .append(createdAt)
                .append(", isActive=")
                .append(isActive)
                .append(", user=")
                .append(user)
                .append('}');
        return sb.toString();
    }

    // Optional: Calculate total amount with tax
    public float getTotalAmountWithTax() {
        return this.totalAmount + this.taxAmount;
    }
}
