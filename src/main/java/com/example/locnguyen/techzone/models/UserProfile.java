package com.example.locnguyen.techzone.models;

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
    @Column(name = "addresss", nullable = false)
    private String address;

    private boolean gender;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private User users;

    public UserProfile(String phoneNumber, String bio, String address, boolean gender, User users) {
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.address = address;
        this.gender = gender;
        this.users = users;
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return "UserProfile{" + "id="
                + id + ", phoneNumber='"
                + phoneNumber + '\'' + ", bio='"
                + bio + '\'' + ", address='"
                + address + '\'' + ", gender="
                + gender + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + ", users="
                + users + '}';
    }

    public UserProfile() {}
}
