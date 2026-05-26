package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> getAllTransactionByAccountId(Long accountId);
}
