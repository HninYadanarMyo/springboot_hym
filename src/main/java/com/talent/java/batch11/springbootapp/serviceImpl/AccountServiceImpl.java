package com.talent.java.batch11.springbootapp.serviceImpl;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.exception.CommonResponse;
import com.talent.java.batch11.springbootapp.exception.ResponseUtils;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.repository.AccountRepository;
import com.talent.java.batch11.springbootapp.service.AccountApiService;
import com.talent.java.batch11.springbootapp.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountApiService, AccountService {

    private final AccountRepository accountRepository;
    private final TokenServiceImpl tokenService;

    @Override
    public Account login(LoginInfo loginInfo) {
        Account account = accountRepository.findAccountByEmail(loginInfo.email());
        if (account == null || !account.getPassword().equals(loginInfo.password())) {
            throw new RuntimeException("Invalid email or password");
        }
        return account;
    }

    @Override
    @Transactional
    public Account withdraw(Account loginAccount, int amount) {
        Account account = accountRepository.findAccountByEmail(loginAccount.getEmail());
        if (account.getBalance() < amount) throw new RuntimeException("Insufficient funds");
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
        if (senderAccount.getBalance() < transferInfo.amount()) throw new RuntimeException("Insufficient funds");
        Account recipientAccount = accountRepository.findAccountByPhoneNumber(transferInfo.receiverPhone());
        if (recipientAccount == null) throw new RuntimeException("Recipient account not found");
        senderAccount.setBalance(senderAccount.getBalance() - transferInfo.amount());
        recipientAccount.setBalance(recipientAccount.getBalance() + transferInfo.amount());
        accountRepository.save(recipientAccount);
        return accountRepository.save(senderAccount);
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
        if (!senderAccount.getPassword().equals(transferInfo.password()))
            throw new RuntimeException("Incorrect password");
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

    //if success -> Common Response
    //if fail -> exception

    @Override
    public ResponseEntity<CommonResponse> handleLoginRequest(LoginInfo loginInfo) {
        String apiName = "login-account";
        Account account = accountRepository.findAccountByEmail(loginInfo.email());
        if (account == null || !account.getPassword().equals(loginInfo.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

        String accessToken = tokenService.generateAccessToken(account);
        String refreshToken = tokenService.generateRefreshToken(account);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", accessToken);
        headers.add("refreshToken", refreshToken);
        ResponseEntity<CommonResponse> responseEntity = ResponseUtils.makeCommonResponse(
                apiName, HttpStatus.OK, account, Boolean.TRUE, "Login Successful"
        );

        return new ResponseEntity<>(responseEntity.getBody(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommonResponse> getAccountByIdResponse(long accountId) {
        if (accountId == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID cannot be zero");
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        account.setTransactions(null);
        return ResponseUtils.makeCommonResponse("get-account-by-id", HttpStatus.OK, account, Boolean.TRUE, "Account Fetched Successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> registerResponse(RegisterInfo registerInfo) {
        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        account.setRoleName("ROLE_USER");
        Account registeredAccount = accountRepository.save(account);

        return ResponseUtils.makeCommonResponse("register-account", HttpStatus.OK, registeredAccount, Boolean.TRUE, "Registration Successful");
    }

    @Override
    public ResponseEntity<CommonResponse> getAllTransactionsResponse(long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return ResponseUtils.makeCommonResponse("get-transactions", HttpStatus.OK, account.getTransactions(), Boolean.TRUE, "Transactions Fetched Successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> withdrawResponse(String email, int amount) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        if (account.getBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = accountRepository.save(account);

        return ResponseUtils.makeCommonResponse("withdraw-money", HttpStatus.OK, updatedAccount, Boolean.TRUE, "Withdrawal Successful");
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> topUpResponse(String email, int amount) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);

        return ResponseUtils.makeCommonResponse("topup-money", HttpStatus.OK, updatedAccount, Boolean.TRUE, "Top Up Successful");
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> depositResponse(String email, int amount) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);

        return ResponseUtils.makeCommonResponse("deposit-money", HttpStatus.OK, updatedAccount, Boolean.TRUE, "Deposit Successful");
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> transferResponse(String senderEmail, TransferInfo transferInfo) {
        Account senderAccount = accountRepository.findAccountByEmail(senderEmail);
        if (senderAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender account not found");
        }
        if (!senderAccount.getPassword().equals(transferInfo.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        if (senderAccount.getBalance() < transferInfo.amount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        Account recipientAccount = accountRepository.findAccountByPhoneNumber(transferInfo.receiverPhone());
        if (recipientAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient account not found");
        }

        senderAccount.setBalance(senderAccount.getBalance() - transferInfo.amount());
        recipientAccount.setBalance(recipientAccount.getBalance() + transferInfo.amount());

        accountRepository.save(recipientAccount);
        Account updatedSender = accountRepository.save(senderAccount);

        return ResponseUtils.makeCommonResponse("transfer-money", HttpStatus.OK, updatedSender, Boolean.TRUE, "Transfer Successful");
    }
    @Override
    public ResponseEntity<CommonResponse> getAllAccountsResponse() {
        List<Account> accounts = accountRepository.findAll();
        return ResponseUtils.makeCommonResponse(
                "get-all-accounts",
                HttpStatus.OK,
                accounts,
                Boolean.TRUE,
                "All Accounts Fetched Successfully"
        );
    }
}
