package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.serviceImpl.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping("/login")
    public ResponseEntity loginAccount(@RequestBody LoginInfo loginInfo) {
        logger.info("Reach Login controller");
        return accountService.handleLoginRequest(loginInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAccountByAccountId(@PathVariable int id) {
        logger.info("Fetching account by ID: {}", id);
        return accountService.getAccountById(id);
    }

    @PostMapping("/register")
    public ResponseEntity createAccount(@RequestBody RegisterInfo registerInfo) {
        logger.info("Registering new account for email: {}", registerInfo.getEmail());
        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        account.setRoleName("ROLE_USER");
        Account registeredAccount = accountService.saveAccount(account);
        logger.info("Account registration successful for email: {}", registerInfo.getEmail());
        return ResponseEntity.ok(registeredAccount);
    }

    @DeleteMapping("/{id}")
    public int deleteAccount(@PathVariable int id) {
        logger.info("Deleting account ID: {}", id);
        return id;
    }

    @PutMapping("/{id}")
    public Account updateAccount(@RequestBody Account account) {
        logger.info("Updating account ID: {}", account.getId());
        return account;
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable int id) {
        logger.info("Fetching transaction history for account ID: {}", id);
        return ResponseEntity.ok(accountService.getAllTransactionsByAccountId(id));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllAccounts() {
        logger.info("Admin fetching all accounts");
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            int amount = request.get("amount");
            logger.info("Entering withdraw API. User: {}, Amount: {}", email, amount);

            Account loginAccount = accountService.findByEmail(email);
            accountService.withdraw(loginAccount, amount);
            Account updatedAccount = accountService.findByEmail(email);

            logger.info("Withdraw successful for User: {}", email);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            logger.error("Withdraw failed for User: {}. Error: {}", email, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/topup")
    public ResponseEntity<?> topUp(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            int amount = request.get("amount");
            logger.info("Entering topup API. User: {}, Amount: {}", email, amount);

            Account loginAccount = accountService.findByEmail(email);
            accountService.topUp(loginAccount, amount);
            Account updatedAccount = accountService.findByEmail(email);

            logger.info("Topup successful for User: {}", email);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            logger.error("Topup failed for User: {}. Error: {}", email, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "fail");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            int amount = request.get("amount");
            logger.info("Entering deposit API. User: {}, Amount: {}", email, amount);

            Account loginAccount = accountService.findByEmail(email);
            accountService.deposit(loginAccount, amount);
            Account updatedAccount = accountService.findByEmail(email);

            logger.info("Deposit successful for User: {}", email);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            logger.error("Deposit failed for User: {}. Error: {}", email, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "fail");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferInfo transferInfo) {
        String senderEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            logger.info("Entering transfer API. Sender: {}, Receiver Phone: {}, Amount: {}",
                    senderEmail, transferInfo.getReceiverPhone(), transferInfo.getAmount());

            Account senderAccount = accountService.findByEmail(senderEmail);
            if (!senderAccount.getPassword().equals(transferInfo.getPassword())) {
                logger.warn("Transfer password verification failed for Sender: {}", senderEmail);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "fail");
                errorResponse.put("message", "Incorrect password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            accountService.transfer(senderAccount, transferInfo);
            Account updatedAccount = accountService.findByEmail(senderEmail);
            logger.info("Transfer completed", senderEmail);
            return ResponseEntity.ok(updatedAccount);
        }catch (Exception e) {
            logger.error("Transfer failed for Sender: {}. Error: {}", senderEmail, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "fail");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}