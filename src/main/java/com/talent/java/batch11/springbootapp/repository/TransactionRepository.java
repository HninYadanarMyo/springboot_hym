package com.talent.java.batch11.springbootapp.repository;

import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction,Long> {
    Iterable<Long> id(Long id);

    List<Transaction> findByTransactionType(TransactionType transactionType);
}
