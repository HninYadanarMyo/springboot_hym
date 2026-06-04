package com.talent.java.batch11.springbootapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Account  extends  AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    private String address;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    private String roleName;


}