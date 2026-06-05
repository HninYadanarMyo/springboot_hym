package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import java.util.List;
import java.util.Map;

public interface AccountApiService {
    Map<String, String> handleLoginRequest(LoginInfo loginInfo);
    Account register(RegisterInfo registerInfo);
    Account getAccountById(long accountId);
    Account withdraw(String email, int amount);
    Account topUp(String email, int amount);
    Account deposit(String email, int amount);
    Account transfer(String senderEmail, TransferInfo transferInfo);
    List<Transaction> getAllTransactionsByAccountId(long accountId);
    List<Account> getAllAccounts();
    Account findByEmail(String email);
}