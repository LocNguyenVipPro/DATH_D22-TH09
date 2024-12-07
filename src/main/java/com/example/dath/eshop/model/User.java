package com.example.dath.eshop.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
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

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> listRoles = new HashSet<>(); // Use HashSet to avoid duplicates

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
        this.createdAt = new Date(); // Automatically set when creating
        this.isActive = true; // Default is active
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", userName='"
                + userName + '\'' + ", firstName='"
                + firstName + '\'' + ", lastName='"
                + lastName + '\'' + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + ", deletedAt="
                + deletedAt + ", isActive="
                + isActive + ", listRoles="
                + listRoles + '}';
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getFullName() {
        if (this.firstName != null && this.lastName != null && !this.firstName.isEmpty() && !this.lastName.isEmpty()) {
            return this.firstName + " " + this.lastName; // Full name as "First Last"
        }
        return "N/A"; // Return a default message if any name field is null or empty
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void addRole(Role role) {
        if (role != null) {
            this.listRoles.add(role); // Avoid adding null roles
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.isActive = true; // Default to active on creation
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}
