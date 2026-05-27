package com.talent.java.batch11.springbootapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Account extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private  String name;

    @Column(nullable = false,unique = true)
    private  String email;

    @Column(nullable = false,unique = true)
    private  String password;

    private  String address;

    @Column(nullable = false,unique = true)
    private  String phoneNumber;

    @Column(nullable = false)
    private double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

}
