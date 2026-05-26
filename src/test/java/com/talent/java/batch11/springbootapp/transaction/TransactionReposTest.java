package com.talent.java.batch11.springbootapp.transaction;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionReposTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository; // 💡 Foreign Key စပ်ဖို့အတွက် Account Repo ပါ လိုအပ်ပါတယ်

//    @Test
//    @Commit
    void saveTransaction() {

        Account account = new Account();
        account.setName("Khin Khin");
        account.setEmail("khinkhin@gmail.com");
        account.setPassword("password123");
        account.setPhoneNumber("098888888");
        account.setBalance(10000.0);
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAccount(savedAccount); // error occurs in setAccountId
//        transaction.setCreatedDate("2026-05-24");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(5000.0);
        transaction.setPreviousAmount(10000.0);

        Transaction savedTransaction = transactionRepository.save(transaction);

        assertNotNull(savedTransaction.getId());


        Transaction retrievedTransaction = transactionRepository.findById(savedTransaction.getId()).orElse(null);
        assertNotNull(retrievedTransaction);

        assertEquals(5000.0, retrievedTransaction.getAmount());
        assertEquals("DEPOSIT", retrievedTransaction.getTransactionType().name());
    }
}