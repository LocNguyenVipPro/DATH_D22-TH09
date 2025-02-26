package com.example.locnguyen.techzone.models;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {}

    public User(String userName, String password, String firstName, String lastName, Date deletedAt, Boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deletedAt = deletedAt;
        this.isActive = isActive;
    }

    public User(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return "Users{" + "id="
                + id + ", userName='"
                + userName + '\'' + ", password='"
                + password + '\'' + ", firstName='"
                + firstName + '\'' + ", lastName='"
                + lastName + '\'' + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + ", deletedAt="
                + deletedAt + ", isActive="
                + isActive + ", role="
                + role + '}';
    }

    public String getFullName() {
        if (this.firstName != null && this.lastName != null && !this.firstName.isEmpty() && !this.lastName.isEmpty()) {
            return this.lastName + " " + this.firstName;
        }
        return null;
    }
}
