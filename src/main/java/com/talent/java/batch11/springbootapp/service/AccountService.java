package com.talent.java.batch11.springbootapp.service;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import java.util.List;

public interface AccountService {
     Account login(LoginInfo loginInfo);
     Account register(RegisterInfo registerInfo);
     Account withdraw(Account loginAccount, int amount);
     Account topUp(Account loginAccount, int amount);
     Account deposit(Account loginAccount, int amount);
     Account transfer(Account loginAccount, TransferInfo transferInfo);
     Account findByEmail(String email);
     List<Account> getAllAccounts();
     List<Transaction> getAllTransactionsByAccountId(long accountId);
     Account getAccountById(long accountId); // ဒါလေးပါ ထည့်ထားပေးပါ
     Account withdraw(String email, int amount); // ဒါလေးပါ
     Account topUp(String email, int amount); // ဒါလေးပါ
     Account deposit(String email, int amount); // ဒါလေးပါ
     Account transfer(String senderEmail, TransferInfo transferInfo); // ဒါလေးပါ
}