package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.request.LoginInfo;
import com.talent.java.batch11.springbootapp.request.TransferInfo;

import java.util.List;

public interface AccountService {
     public void register(Account account);
     public Account login(LoginInfo loginInfo);
     public Account saveAccount(Account account);
     public Account findByEmail(String email);
     public Account findByPhoneNumber(String phoneNumber);
     public void updateBalanceById(Long accountId, double newBalance);
     public List<Account> getAllAccounts();
     public List<Transaction> getAllTransactionsByAccountId(long accountId);
      //hw
     public void deposit(Account account,double amount);
     public void topUp(Account account,double amount);
     public void withdraw(Account account,double amount);
     public void transfer(Account account, TransferInfo transferInfo);
}