package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.model.Account;
import jakarta.transaction.Transactional;

public interface AccountService {
     Account saveAccount(Account account);
     Account login(String email,String password);
     Account findAccountByEmail(String email);
     Account findAccountByPhoneNumber(String phoneNumber);
     void deposit(Long accountId,double amount);
     void withDraw(Long accountId, double amount);
     void topUp(Long accountId, double amount);
     void transfer(Long ownerId, String receiverPhone, double amount, String password);

     void updateBalanceById(long accountId, double newBalance);
}