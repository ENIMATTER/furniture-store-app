package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.model.CardDto;
import com.epam.furniturestoreapp.model.Roles;
import com.epam.furniturestoreapp.model.UserSignUpDto;
import com.epam.furniturestoreapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
public class AccountController {
    private final UserTableService userTableService;
    private final CategoryService categoryService;

    @Autowired
    public AccountController(CategoryService categoryService, UserTableService userTableService) {
        this.userTableService = userTableService;
        this.categoryService = categoryService;
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        addToModelBasicAttributes(model);
        return "signup";
    }

    @PostMapping("/signup")
    @PreAuthorize("!@userTableService.existsByEmail(#userSignUp.email) " +
            "&& #userSignUp.userPassword.equals(#userSignUp.userPasswordAgain)")
    public String postSignup(@Valid @ModelAttribute("userSignUp") UserSignUpDto userSignUp,
                             BindingResult result, Model model) {
        addToModelBasicAttributes(model);
        if (result.hasErrors()) {
            model.addAttribute("fail", true);
            return "signup";
        }
        double balance = 0.0;

        String codedPassword = new BCryptPasswordEncoder().encode(userSignUp.getUserPassword());
        UserTable user = new UserTable(userSignUp.getFirstname(), userSignUp.getLastname(),
                userSignUp.getEmail(), codedPassword, userSignUp.getPhoneNumber(), balance, Roles.USER.name());
        userTableService.addUser(user);
        return "login";
    }

    // Spring Security custom login form (email like username)
    @GetMapping("/login")
    public String getLogin(Model model) {
        addToModelBasicAttributes(model);
        return "login";
    }

    // Spring Security custom logout
    @GetMapping("/logout")
    public String customLogout(HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        addToModelBasicAttributes(model);
        model.addAttribute("logout", true);
        return "login";
    }

    @GetMapping("/account")
    public String account(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        double balance = user.getBalance();
        model.addAttribute("balance", balance);
        addToModelBasicAttributes(model);
        return "account";
    }

    @DeleteMapping("/account")
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response, Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        userTableService.deleteUser(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        addToModelBasicAttributes(model);
        return "index";
    }

    @GetMapping("/top-up")
    public String getTopUp(Model model) {
        int year = LocalDateTime.now().getYear();
        int monthInt = LocalDateTime.now().getMonthValue();
        String month = monthInt + "";
        if (monthInt < 10) {
            month = "0" + month;
        }
        String min = year + "-" + month;
        model.addAttribute("min", min);
        addToModelBasicAttributes(model);
        return "top-up";
    }

    @PostMapping("/top-up")
    public String postTopUp(@RequestParam("amount") Double amount, Model model,
                            @Valid @ModelAttribute("cardDto") CardDto cardDto, BindingResult result) {
        addToModelBasicAttributes(model);
        if (result.hasErrors()) {
            model.addAttribute("fail", true);
            return "top-up";
        }
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        user.setBalance(user.getBalance() + amount);
        userTableService.editUser(user);

        double balance = user.getBalance();
        model.addAttribute("balance", balance);
        return "account";
    }

    private void addToModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}