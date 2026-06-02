package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import com.talent.java.batch11.springbootapp.repository.TransactionRepository;
import com.talent.java.batch11.springbootapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransactionHistory(Account account, double amount, String type, double previousAmount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.valueOf(type));
        transaction.setPreviousAmount(previousAmount);
        transaction.setAccount(account);
        transaction.setCreatedAt(java.time.LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getTransactionsByType(String type) {
        TransactionType enumType = TransactionType.valueOf(type.toUpperCase());
        return transactionRepository.findByTransactionType(enumType);
    }

}