package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.dto.request.LoginInfo;
import com.talent.java.batch11.springbootapp.dto.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.dto.request.TransferInfo;
import com.talent.java.batch11.springbootapp.dto.request.WithdrawRequest;
import com.talent.java.batch11.springbootapp.dto.response.AdminLoginResponse;
import com.talent.java.batch11.springbootapp.dto.response.UserLoginResponse;
import com.talent.java.batch11.springbootapp.dto.response.WithdrawResponse;
import com.talent.java.batch11.springbootapp.service.AccountService;
import com.talent.java.batch11.springbootapp.repository.AccountRepository; // repository လိုအပ်လို့ import တိုးထားပါတယ်
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerAccount(@RequestBody RegisterInfo registerInfo) {        //
        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        account.setRole(registerInfo.getRole());

        Account registeredAccount = accountService.saveAccount(account);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registration successful");
        response.put("account", registeredAccount);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginAccount(@RequestBody LoginInfo loginInfo) {      //<Obj> -> accept both data type(user,admin)
        Account account = accountService.login(loginInfo);
        if (account != null && "ADMIN".equals(account.getRole())) {
            AdminLoginResponse adminResponse = new AdminLoginResponse();
            adminResponse.setAccounts(accountService.getAllAccounts());
            adminResponse.setTransactions(accountService.getAllTransactionsByAccountId(account.getId()));
            return ResponseEntity.ok(adminResponse);
        }

        UserLoginResponse userResponse = new UserLoginResponse();
        userResponse.setAccount(account);
        userResponse.setTransactions(accountService.getAllTransactionsByAccountId(account.getId()));
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return ResponseEntity.ok(account);
    }
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable("id") long id) {
        return ResponseEntity.ok(accountService.getAllTransactionsByAccountId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccountProfile(@PathVariable("id") long id, @RequestBody RegisterInfo updateInfo) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setName(updateInfo.getName());
        existingAccount.setEmail(updateInfo.getEmail());
        existingAccount.setPhoneNumber(updateInfo.getPhoneNumber());
        existingAccount.setAddress(updateInfo.getAddress());
        if(updateInfo.getPassword() != null) {
            existingAccount.setPassword(updateInfo.getPassword());
        }

        Account savedAccount = accountRepository.save(existingAccount);
        return ResponseEntity.ok(savedAccount);
    }
    @PatchMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> updateBalance(@PathVariable("id") long id, @RequestParam("amount") double amount, @RequestParam("action") String action) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if ("DEPOSIT".equalsIgnoreCase(action)) {
            accountService.deposit(account, amount);
        } else if ("WITHDRAW".equalsIgnoreCase(action)) {
            accountService.withdraw(account, amount);
        } else if ("TOPUP".equalsIgnoreCase(action)) {
            accountService.topUp(account, amount);
        }

        Account updatedAccount = accountRepository.findById(id).orElseThrow();
        Map<String, Object> response = new HashMap<>();
        response.put("message", action + " successful");
        response.put("newBalance", updatedAccount.getBalance());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(
            @RequestParam("accountId") long accountId,
            @RequestBody TransferInfo transferInfo) {

        List<Account> all = accountService.getAllAccounts();
        Account account = all.stream()
                .filter(a -> a.getId() == accountId)
                .findFirst()
                .orElseThrow();

        accountService.transfer(account, transferInfo);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Transfer successful");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteAccount(@PathVariable("id") Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account Not Found"));
        accountRepository.delete(account);
        Map<String,String> response = new HashMap<>();
        response.put("message","Account with ID "+id+" has been successfully deleted");
        return ResponseEntity.ok(response);
    }
}