package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.model.enumType.TransactionType;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.repository.TransactionRepository;
import com.talent.java.batch11.springbootapp.request.LoginInfo;
import com.talent.java.batch11.springbootapp.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void saveAccount(Account account) {
        try {
            System.out.println("Saving Account " + account);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException("Account can't save");
        }
    }


    @Override
    public void login(LoginInfo loginInfo) {
        Account account = findAccountByEmail(loginInfo.getEmail());
        if (account == null) {
            throw new RuntimeException("Account does not exist.");
        }
        if (!account.getPassword().equals(loginInfo.getPassword())) {
            throw new RuntimeException("Incorrect Password!");
        }
    }

    @Override
    public void logout() {

    }

    @Override
    public void registerAccount(Account account) {

    }


    @Override
    public Account findAccountByEmail(String email){
        if (email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be null");
        }
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account findAccountByPhoneNumber(String phoneNumber){
        return accountRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    @Override
    public void deposit(Long accountId,double amount){
        Account account = getAccountById(accountId);
        typeOfTransaction(account,amount,"DEPOSIT",true);
    }

    @Transactional
    @Override
    public void withDraw(Long accountId, double amount){
        Account account = getAccountById(accountId);
        typeOfTransaction(account,amount,"WITHDRAW",false);
    }

    @Transactional
    @Override
    public void topUp(Long accountId, double amount){
        Account account = getAccountById(accountId);
        typeOfTransaction(account,amount,"TOP_UP",false);
    }

    @Transactional
    @Override
    public void transfer(Long ownerId,String receiverPhone, double amount, String password){
        Account sender = getAccountById(ownerId);
        if (!sender.getPassword().equals(password)){
            throw new RuntimeException("Incorrect Password");
        }
        Account receiver = findAccountByPhoneNumber(receiverPhone);
        if (receiver == null){
            throw new RuntimeException("Receiver phone number not found");
        }
        if (sender.equals(receiver)){
            throw new RuntimeException("Cannot transfer to same account");
        }
        typeOfTransaction(sender,amount,"TRANSFER",false);
        typeOfTransaction(receiver,amount,"TRANSFER",true);
    }

    private void typeOfTransaction(Account account,double amount, String transactionType, boolean isAdd){
        double previousBalance = account.getBalance();
        if(amount <= 0){
            throw new RuntimeException("Amount must be greater than 0");
        }
        if(!isAdd && previousBalance < amount){
            throw new RuntimeException("Insufficient balance");
        }
        if(isAdd){
            account.setBalance(previousBalance + amount);
        }else{
            account.setBalance(previousBalance - amount);
        }
        accountRepository.save(account);
        createTransactionHistory(account, transactionType, amount, previousBalance);
    }

    private Account getAccountById(Long accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(()->new RuntimeException("Account Not Found with id: "+accountId));
    }
    private void createTransactionHistory(Account account,String type,double amount,double previousAmount){
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setPreviousAmount(previousAmount);
        tx.setTransactionType(TransactionType.valueOf(type));
        transactionRepository.save(tx);
    }
    @Transactional
    @Override
    public void updateBalanceById(long accountId, double newBalance){
        Account account = getAccountById(accountId);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

}