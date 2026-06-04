package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TokenServiceImpl tokenService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Account login(LoginInfo loginInfo) {
        Account account = accountRepository.findAccountByEmail(loginInfo.getEmail());
        if (account == null || !account.getPassword().equals(loginInfo.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return account;
    }

    @Override
    public ResponseEntity handleLoginRequest(LoginInfo loginInfo) {

        logger.info("Reach Login controller");

        Account account = accountRepository.findAccountByEmail(loginInfo.getEmail());
        if (account == null || !account.getPassword().equals(loginInfo.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = tokenService.generateAccessToken(account);
        String refreshToken = tokenService.generateRefreshToken(account);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", accessToken);
        headers.add("refreshToken", refreshToken);

        return new ResponseEntity<>(account, headers, HttpStatus.OK);
    }

    @Override
    public void deposit(Account loginAccount, int amount) {
        loginAccount.setBalance(loginAccount.getBalance() + amount);
        accountRepository.save(loginAccount);
    }

    @Override
    public void withdraw(Account loginAccount, int amount) {
        if (loginAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        loginAccount.setBalance(loginAccount.getBalance() - amount);
        accountRepository.save(loginAccount);

    }

    @Override
    public void topUp(Account loginAccount, int amount) {
        loginAccount.setBalance(loginAccount.getBalance() + amount);
        accountRepository.save(loginAccount);
    }


    @Override
    public void transfer(Account loginAccount, TransferInfo transferInfo) {
        if (loginAccount.getBalance() < transferInfo.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        Account recipientAccount = accountRepository.findAccountByPhoneNumber(transferInfo.getReceiverPhone());
        if (recipientAccount == null) {
            throw new RuntimeException("Recipient account not found");
        }
        loginAccount.setBalance(loginAccount.getBalance() - transferInfo.getAmount());
        recipientAccount.setBalance(recipientAccount.getBalance() + transferInfo.getAmount());
        accountRepository.save(loginAccount);
        accountRepository.save(recipientAccount);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        try {
            System.out.println("Saving Account " + account);
            return accountRepository.save(account);
        } catch (Exception e) {
            logger.error("Error saving Account " + account, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public Account findByPhoneNumber(String phoneNumber) {
        return accountRepository.findAccountByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public void updateBalanceById(Long accountId, double newBalance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<Transaction> getAllTransactionsByAccountId(long accountId) {

        Account account = accountRepository.findAccountById(accountId);
        return account.getTransactions();
    }

    @Override
    public ResponseEntity getAccountById(long accountId) {
        Account account;
        try {

            if (accountId == 0) {
                return ResponseEntity.noContent().build();
            }
            account = accountRepository.findAccountById(accountId);
            if (account == null) {
                return ResponseEntity.notFound().build();
            }
            account.setTransactions(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(account);
    }
}