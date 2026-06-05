package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.service.AccountApiService;
import com.talent.java.batch11.springbootapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountApiService, AccountService {

    private final AccountRepository accountRepository;
    private final TokenServiceImpl tokenService;

    @Override
    public Account login(LoginInfo loginInfo) {
        Account account = accountRepository.findAccountByEmail(loginInfo.getEmail());
        if (account == null || !account.getPassword().equals(loginInfo.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return account;
    }

    @Override
    @Transactional
    public Account withdraw(Account loginAccount, int amount) {
        Account account = accountRepository.findAccountByEmail(loginAccount.getEmail());
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account topUp(Account loginAccount, int amount) {
        Account account = accountRepository.findAccountByEmail(loginAccount.getEmail());
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account deposit(Account loginAccount, int amount) {
        Account account = accountRepository.findAccountByEmail(loginAccount.getEmail());
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account transfer(Account loginAccount, TransferInfo transferInfo) {
        Account senderAccount = accountRepository.findAccountByEmail(loginAccount.getEmail());

        if (senderAccount.getBalance() < transferInfo.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        Account recipientAccount = accountRepository.findAccountByPhoneNumber(transferInfo.getReceiverPhone());
        if (recipientAccount == null) {
            throw new RuntimeException("Recipient account not found");
        }

        senderAccount.setBalance(senderAccount.getBalance() - transferInfo.getAmount());
        recipientAccount.setBalance(recipientAccount.getBalance() + transferInfo.getAmount());

        accountRepository.save(recipientAccount);
        return accountRepository.save(senderAccount);
    }

    @Override
    public Map<String, String> handleLoginRequest(LoginInfo loginInfo) {
        Account account = login(loginInfo);
        String accessToken = tokenService.generateAccessToken(account);
        String refreshToken = tokenService.generateRefreshToken(account);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Override
    @Transactional
    public Account register(RegisterInfo registerInfo) {
        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        account.setRoleName("ROLE_USER");
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(long accountId) {
        if (accountId == 0) throw new IllegalArgumentException("Account ID cannot be zero");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) throw new RuntimeException("Account not found");
        account.setTransactions(null);
        return account;
    }

    @Override
    @Transactional
    public Account withdraw(String email, int amount) {
        Account account = findByEmail(email);
        if (account.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account topUp(String email, int amount) {
        Account account = findByEmail(email);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account deposit(String email, int amount) {
        Account account = findByEmail(email);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account transfer(String senderEmail, TransferInfo transferInfo) {
        Account senderAccount = findByEmail(senderEmail);
        if (!senderAccount.getPassword().equals(transferInfo.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        return transfer(senderAccount, transferInfo);
    }

    @Override
    public List<Transaction> getAllTransactionsByAccountId(long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) throw new RuntimeException("Account not found");
        return account.getTransactions();
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByEmail(String email) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) throw new RuntimeException("Account not found");
        return account;
    }
}