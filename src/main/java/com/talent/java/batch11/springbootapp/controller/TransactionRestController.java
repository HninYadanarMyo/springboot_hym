package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    @Autowired
    private TransactionService transactionService;
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@RequestParam("type") String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }
}