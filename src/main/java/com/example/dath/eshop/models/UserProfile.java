package com.example.dath.eshop.models;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @Column(name = "bio", columnDefinition = "TEXT", nullable = false)
    private String bio;

    @NotNull
    @Column(name = "address", nullable = false) // Fixed typo from 'addresss' to 'address'
    private String address;

    @Column(name = "gender")
    private boolean gender; // Using primitive type 'boolean'

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Updated CascadeType
    @JoinColumn(name = "users_id")
    private User users;

    public UserProfile(String phoneNumber, String bio, String address, boolean gender, User users) {
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.address = address;
        this.gender = gender;
        this.users = users;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserProfile{")
                .append("id=")
                .append(id)
                .append(", phoneNumber='")
                .append(phoneNumber)
                .append('\'')
                .append(", bio='")
                .append(bio)
                .append('\'')
                .append(", address='")
                .append(address)
                .append('\'')
                .append(", gender=")
                .append(gender)
                .append(", createdAt=")
                .append(createdAt)
                .append(", updatedAt=")
                .append(updatedAt)
                .append(", users=")
                .append(users)
                .append('}');
        return sb.toString();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date(); // Automatically set updatedAt when the entity is updated
    }

    public UserProfile() {}
}
