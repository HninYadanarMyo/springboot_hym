package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.request.LoginInfo;
import com.talent.java.batch11.springbootapp.request.TransferInfo;
import com.talent.java.batch11.springbootapp.service.AccountService;
import com.talent.java.batch11.springbootapp.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionService transactionService;

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
    public Account saveAccount(Account account) {
        try {
            System.out.println("Saving Account " + account );
            return accountRepository.save(account);
        } catch (Exception e) {
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
        return  account.getTransactions();
    }



    @Override
    @Transactional
    public void deposit(Account account, double amount) {
        double previousBalance = account.getBalance();
        double newBalance = previousBalance + amount;
        this.updateBalanceById(account.getId(), newBalance);

        transactionService.saveTransactionHistory(account, amount, "DEPOSIT", previousBalance);
    }

    @Override
    @Transactional
    public void withdraw(Account account, double amount) {
        Account realAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException("Can't find account"));

        if (realAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double previousBalance = realAccount.getBalance();
        double newBalance = previousBalance - amount;

        realAccount.setBalance(newBalance);
        accountRepository.save(realAccount);

        transactionService.saveTransactionHistory(realAccount, amount, "WITHDRAW", previousBalance);
    }

    @Override
    @Transactional
    public void topUp(Account account, double amount) {
        Account realAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException("Can't find account"));

        if (realAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double previousBalance = realAccount.getBalance();
        double newBalance = previousBalance - amount;

        realAccount.setBalance(newBalance);
        accountRepository.save(realAccount);

        transactionService.saveTransactionHistory(realAccount, amount, "TOP_UP", previousBalance);
    }

    @Override
    @Transactional
    public void transfer(Account account, TransferInfo transferInfo) {
        Account sender = accountRepository.findById(account.getId())
                .orElseThrow(() -> new RuntimeException("Can't find account"));

        if (!sender.getPassword().equals(transferInfo.getPassword())) {
            throw new RuntimeException("Incorrect Password");
        }

        if (sender.getBalance() < transferInfo.getAmount()) {
            throw new RuntimeException("Insufficient Balance");
        }

        Account receiver = accountRepository.findAccountByPhoneNumber(transferInfo.getReceiverPhone());
        if (receiver == null) {
            throw new RuntimeException("Can't find receiver phone number");
        }
        double senderPreviousBalance = sender.getBalance();
        sender.setBalance(senderPreviousBalance - transferInfo.getAmount());
        accountRepository.save(sender);
        transactionService.saveTransactionHistory(sender, transferInfo.getAmount(), "TRANSFER", senderPreviousBalance);
        double receiverPreviousBalance = receiver.getBalance();
        receiver.setBalance(receiverPreviousBalance + transferInfo.getAmount());
        accountRepository.save(receiver);
        transactionService.saveTransactionHistory(receiver, transferInfo.getAmount(), "TRANSFER", receiverPreviousBalance);
    }

}

