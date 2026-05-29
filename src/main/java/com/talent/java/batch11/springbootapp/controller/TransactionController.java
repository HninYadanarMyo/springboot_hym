package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.model.Transaction;
import com.talent.java.batch11.springbootapp.service.AccountService;
import com.talent.java.batch11.springbootapp.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class TransactionController {
//    @Autowired
//    AccountService accountService;
//
//    @GetMapping("/history")
//    public String showHistory(HttpSession session, Model model) {
//        Account loginAccount = (Account) session.getAttribute("accountInfo");
//        if (loginAccount == null) {
//            return "redirect:/login";
//        }
//        Account currentAccount = accountService.findByEmail(loginAccount.getEmail());
//        model.addAttribute("currentAccount", currentAccount);
//        model.addAttribute("transactions", currentAccount.getTransactions());
//
//        return "history";
//    }
}
