package com.talent.java.batch11.springbootapp.model;

import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Entity
@Table(name = "transactions")
@Data
public class Transaction extends  AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private  double amount;
    private  double previousAmount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}