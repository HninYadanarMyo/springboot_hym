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
    public String registerAccount(@ModelAttribute("registerInfo") RegisterInfo registerInfo, HttpSession session) {
        Account account = new Account();
        BeanUtils.copyProperties(registerInfo, account, "id");
        account.setBalance(0);
        account.setRole(registerInfo.getRole());

        Account registeredAccount = accountService.saveAccount(account);
        session.setAttribute("accountInfo", registeredAccount);

        if (registeredAccount != null && "ADMIN".equals(registeredAccount.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginInfo loginInfo = new LoginInfo();
        model.addAttribute("logininfo", loginInfo);
        return "login";
    }

    @PostMapping("/loginAccount")
    public String loginAccount(@ModelAttribute("logininfo") LoginInfo loginInfo, HttpSession session) {
        Account account = accountService.login(loginInfo);
        session.setAttribute("accountInfo", account);

        if (account != null && "ADMIN".equals(account.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboardPage(Model model, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        if (loginAccount == null || !"ADMIN".equals(loginAccount.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("currentAccount", loginAccount);
        model.addAttribute("allaccount", accountService.getAllAccounts());
        return "admin_dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentAccount", loginAccount);
        return "dashboard";
    }

    @GetMapping("/history")
    public String historyPage(Model model, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }
        Account currentAccount = accountService.findByEmail(loginAccount.getEmail());
        model.addAttribute("currentAccount", currentAccount);
        model.addAttribute("transactions", accountService.getAllTransactionsByAccountId(currentAccount.getId()));
        return "history";
    }

    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute("amount") int amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.withdraw(loginAccount, amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return "redirect:/dashboard";
    }

    @PostMapping("/topup")
    public String topUp(@ModelAttribute("amount") int amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.topUp(loginAccount, amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute("transferInfo") TransferInfo transferInfo, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.transfer(loginAccount, transferInfo);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return "redirect:/dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@ModelAttribute("amount") int amount, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("accountInfo");
        accountService.deposit(loginAccount, amount);
        Account updatedAccount = accountService.findByEmail(loginAccount.getEmail());
        session.setAttribute("accountInfo", updatedAccount);
        return "redirect:/dashboard";
    }
}