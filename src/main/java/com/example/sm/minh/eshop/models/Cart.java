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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float taxAmount;

    private Float totalAmount;

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
    private Boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private User user;

    public Cart() {
        this.countItem = 0;
        this.createdAt = new Date();
        this.taxAmount = 0;
        this.totalAmount = 0f;
    }

    @Override
    public String toString() {
        return "Cart{" + "id="
                + id + ", tax_amount="
                + taxAmount + ", total_amount="
                + totalAmount + ", count_items="
                + countItem + ", updatedAt="
                + updatedAt + ", deletedAt="
                + deletedAt + ", createdAt="
                + createdAt + ", isActive="
                + isActive + ", usersId="
                + user + '}';
    }
}
