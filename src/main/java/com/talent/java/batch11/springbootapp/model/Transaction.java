package com.talent.java.batch11.springbootapp.model;

import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    private  double amount;
    private  double previousAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;

}
