package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions/{accountId}")
    public String viewTransactionHistory(@PathVariable Long accountId, Model model) {

        List<Transaction> transactionList = transactionService.getAllTransactionByAccountId(accountId);

        model.addAttribute("transactions", transactionList);
        model.addAttribute("accountId", accountId);

        return "transaction_history";
    }
}