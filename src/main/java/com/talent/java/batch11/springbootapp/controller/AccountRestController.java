package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.service.AccountApiService;
import com.talent.java.batch11.springbootapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccountApiService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody LoginInfo loginInfo) {
        logger.info("Reach Login controller");
        Map<String, String> tokens = accountService.handleLoginRequest(loginInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", tokens.get("accessToken"));
        headers.add("refreshToken", tokens.get("refreshToken"));

        Account account = accountService.findByEmail(loginInfo.getEmail());
        return new ResponseEntity<>(account, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountByAccountId(@PathVariable int id) {
        logger.info("Fetching account by ID: {}", id);
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody RegisterInfo registerInfo) {
        logger.info("Registering new account for email: {}", registerInfo.getEmail());
        Account registeredAccount = accountService.register(registerInfo);
        return ResponseEntity.ok(registeredAccount);
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
        int amount = request.get("amount");
        logger.info("Withdraw API. User: {}, Amount: {}", email, amount);

        Account updatedAccount = accountService.withdraw(email, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/topup")
    public ResponseEntity<?> topUp(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int amount = request.get("amount");
        logger.info("Topup API. User: {}, Amount: {}", email, amount);

        Account updatedAccount = accountService.topUp(email, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int amount = request.get("amount");
        logger.info("Deposit API. User: {}, Amount: {}", email, amount);

        Account updatedAccount = accountService.deposit(email, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferInfo transferInfo) {
        String senderEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Transfer API. Sender: {}, Receiver Phone: {}", senderEmail, transferInfo.getReceiverPhone());

        Account updatedAccount = accountService.transfer(senderEmail, transferInfo);
        return ResponseEntity.ok(updatedAccount);
    }
}