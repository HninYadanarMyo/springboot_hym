package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;

import java.util.List;

public interface TransactionService {
    void saveTransactionHistory(Account account, double amount, String type, double previousAmount);

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(long id);

    List<Transaction> getTransactionsByType(String type);
}
