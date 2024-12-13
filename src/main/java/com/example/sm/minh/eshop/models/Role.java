package com.example.sm.minh.eshop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Column(length = 45,nullable = false,unique = true)
    private String name;

    @Column(name = "descripton")
    private String des;

    @ManyToMany(mappedBy = "listRoles",fetch = FetchType.EAGER)
    private Set<User> list_User=new HashSet<>();

    public Role(String name, String des) {
        this.name = name;
        this.des = des;
    }


    public Role() {
    }
}
