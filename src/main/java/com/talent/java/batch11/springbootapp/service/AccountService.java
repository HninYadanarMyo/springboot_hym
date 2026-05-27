package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.request.LoginInfo;

import java.util.List;

public interface AccountService {

     public void login(LoginInfo loginInfo);
     public void logout();
     public void registerAccount(Account account);
     public void saveAccount(Account account);
     public Account findAccountByEmail(String email);
     public Account findAccountByPhoneNumber(String phoneNumber);
     void deposit(Long accountId,double amount);
     void withDraw(Long accountId, double amount);
     void topUp(Long accountId, double amount);
     void transfer(Long ownerId, String receiverPhone, double amount, String password);
     public void updateBalanceById(long accountId, double newBalance);
     public List<Account> getAllAccounts();

}