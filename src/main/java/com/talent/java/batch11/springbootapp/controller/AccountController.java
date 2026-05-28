package com.talent.java.batch11.springbootapp.controller;

import com.talent.java.batch11.springbootapp.model.Account;
import com.talent.java.batch11.springbootapp.request.LoginInfo;
import com.talent.java.batch11.springbootapp.request.RegisterInfo;
import com.talent.java.batch11.springbootapp.request.TransferInfo;
import com.talent.java.batch11.springbootapp.serviceImpl.AccountServiceImpl;
import jakarta.servlet.http.HttpSession;
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
        return "index";
    }

    @GetMapping("/register")
    //to show register page
    public String register(Model model) {
        RegisterInfo registerInfo = new RegisterInfo();
        model.addAttribute("registerInfo", registerInfo);
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/registerAccount", method = RequestMethod.POST)
    // to save account after submit register page
    public String registerAccount(@ModelAttribute("registerInfo") RegisterInfo registerInfo, HttpSession session) {

        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        Account registeredAccount = accountService.saveAccount(account);
        session.setAttribute("accountInfo", registeredAccount);

        return  "redirect:/dashboard";
    }

    @GetMapping("/login")
    // to show login page
    public String login(Model model) {
        LoginInfo loginInfo = new LoginInfo();
        model.addAttribute("logininfo", loginInfo);
        return "login";
    }

    @PostMapping("/loginAccount")
    //after submit login page
    public String loginAccount(@ModelAttribute("logininfo") LoginInfo loginInfo, HttpSession session) {

        Account account = accountService.login(loginInfo);
        session.setAttribute("accountInfo", account);

        return  "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute("amount") int  amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
         accountService.withdraw(loginAccount,amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return  "redirect:/dashboard";
    }

    @PostMapping("/topup")
    public String topUp(@ModelAttribute("amount") int  amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.topUp(loginAccount,amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return  "redirect:/dashboard";
    }
    @PostMapping("/transfer")
    public String transfer(@ModelAttribute("transferInfo") TransferInfo transferInfo, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.transfer(loginAccount,transferInfo);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return  "redirect:/dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@ModelAttribute("amount") int  amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.deposit(loginAccount,amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return  "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    // to show dashboard page
    public String dashboardPage(Model model, HttpSession session) {

        Account loginAccount = (Account) session.getAttribute("accountInfo");
        model.addAttribute("currentAccount", loginAccount);
        model.addAttribute("allaccount", accountService.getAllAccounts());
        model.addAttribute("transactions",
                accountService.getAllTransactionsByAccountId(loginAccount.getId
                        ()));
        return "dashboard";
    }
}

