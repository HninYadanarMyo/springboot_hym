package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
     public Account login(LoginInfo loginInfo);
     public Account saveAccount(Account account);
     public Account findByEmail(String email);
     public Account findByPhoneNumber(String phoneNumber);
     public void updateBalanceById(Long accountId, double newBalance);
     public List<Account> getAllAccounts();
     public List<Transaction> getAllTransactionsByAccountId(long accountId);

     public ResponseEntity getAccountById(long accountId);

     public ResponseEntity handleLoginRequest(LoginInfo loginInfo);

     public void deposit(Account loginAccount, int amount);

     public void withdraw(Account loginAccount, int amount);

     public void topUp(Account loginAccount, int amount);

     public void transfer(Account loginAccount, TransferInfo transferInfo);


}