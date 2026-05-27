package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.request.LoginInfo;
import com.talent.java.batch11.springbootapp.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.serviceImpl.AccountServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {

    @Autowired
    AccountServiceImpl accountService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("allaccount", accountService.getAllAccounts());
        return "index";
    }

    @GetMapping("/register")
    //to show register page
    public String register(Model model) {
        RegisterInfo registerInfo = new RegisterInfo();
        model.addAttribute("registerInfo", registerInfo);
        return "register";
    }

    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    // to save account after submit register page
    public String registerAccount(@ModelAttribute("registerInfo") RegisterInfo registerInfo) {

        Account account = new Account();
        account.setId(null);
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        accountService.saveAccount(account);
        return  "redirect:/";
    }
//        account.setCreatedAt( java.time.LocalDateTime.now());
//        account.setCreatedBy(registerInfo.getName());
//        account.setUpdatedAt( java.time.LocalDateTime.now());
//        account.setUpdatedBy(registerInfo.getName());


    @GetMapping("/login")
    // to show login page
    public String login(Model model) {
        LoginInfo loginInfo = new LoginInfo();
        model.addAttribute("logininfo", loginInfo);
        return "login";
    }

    @PostMapping("/loginAccount")
    //after submit login page
    public String loginAccount(@ModelAttribute("logininfo") LoginInfo loginInfo) {
        accountService.login(loginInfo);
        return  "redirect:/";
    }
    //Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam("accountId") Long accountId,
                          @RequestParam("amount") double amount) {
        accountService.deposit(accountId, amount);
        return "redirect:/";
    }

    //Withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") double amount) {
        accountService.withDraw(accountId, amount);
        return "redirect:/";
    }

    // Top-Up
    @PostMapping("/topup")
    public String topUp(@RequestParam("accountId") Long accountId,
                        @RequestParam("amount") double amount) {
        accountService.topUp(accountId, amount);
        return "redirect:/";
    }

    //  Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestParam("ownerId") Long ownerId,
                           @RequestParam("receiverPhone") String receiverPhone,
                           @RequestParam("amount") double amount,
                           @RequestParam("password") String password) {
        accountService.transfer(ownerId, receiverPhone, amount, password);
        return "redirect:/";
    }
    //logout
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }



}