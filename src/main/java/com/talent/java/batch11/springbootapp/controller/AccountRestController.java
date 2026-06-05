package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.exception.CommonResponse;
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
    private final AccountApiService accountApiService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> loginAccount(@RequestBody LoginInfo loginInfo) {
        logger.info("Reach Login controller");
        return accountApiService.handleLoginRequest(loginInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getAccountByAccountId(@PathVariable long id) {
        logger.info("Fetching account by ID: {}", id);
        return accountApiService.getAccountByIdResponse(id);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> createAccount(@RequestBody RegisterInfo registerInfo) {
        logger.info("Registering new account for email: {}", registerInfo.email());
        return accountApiService.registerResponse(registerInfo);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<CommonResponse> getHistory(@PathVariable long id) {
        logger.info("Fetching transaction history for account ID: {}", id);
        return accountApiService.getAllTransactionsResponse(id);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllAccounts() {
        logger.info("Admin fetching all accounts");
        return ResponseEntity.ok(accountApiService.getAllAccountsResponse());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdraw(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int amount = request.get("amount");
        logger.info("Withdraw API. User: {}, Amount: {}", email, amount);
        return accountApiService.withdrawResponse(email, amount);
    }

    @PostMapping("/topup")
    public ResponseEntity<CommonResponse> topUp(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int amount = request.get("amount");
        logger.info("Topup API. User: {}, Amount: {}", email, amount);
        return accountApiService.topUpResponse(email, amount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<CommonResponse> deposit(@RequestBody Map<String, Integer> request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int amount = request.get("amount");
        logger.info("Deposit API. User: {}, Amount: {}", email, amount);
        return accountApiService.depositResponse(email, amount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<CommonResponse> transfer(@RequestBody TransferInfo transferInfo) {
        String senderEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Transfer API. Sender: {}, Receiver Phone: {}", senderEmail, transferInfo.receiverPhone());
        return accountApiService.transferResponse(senderEmail, transferInfo);
    }

}