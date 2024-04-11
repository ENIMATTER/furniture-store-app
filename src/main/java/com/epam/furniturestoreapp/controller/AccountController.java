package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
public class AccountController {
    private final UserTableService userTableService;
    private final AddressService addressService;
    private final ReviewService reviewService;
    private final OrderTableService orderTableService;
    private final OrderItemService orderItemService;
    private final List<Category> categories;

    @Autowired
    public AccountController(CategoryService categoryService, UserTableService userTableService,
                             AddressService addressService, ReviewService reviewService,
                             OrderTableService orderTableService, OrderItemService orderItemService) {
        this.userTableService = userTableService;
        this.addressService = addressService;
        this.reviewService = reviewService;
        this.categories = categoryService.findAll();
        this.orderTableService = orderTableService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        addToModelBasicAttributes(model);
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignup(@RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("email") String email,
                             @RequestParam("userPassword") String userPassword,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("street") String street,
                             @RequestParam("city") String city,
                             @RequestParam("state") String state,
                             @RequestParam("country") String country,
                             @RequestParam("zipCode") String zipCode,
                             Model model) {

        addToModelBasicAttributes(model);
        if (userTableService.existsByEmail(email)) {
            return "redirect:/error-page";
        }

        String codedPassword = new BCryptPasswordEncoder().encode(userPassword);
        UserTable user = new UserTable();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUserPassword(codedPassword);
        user.setPhoneNumber(phoneNumber);
        user.setBalance(0);
        user.setRoles("USER");
        userTableService.addUser(user);

        Address address = new Address();
        address.setUserTableID(user);
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zipCode);
        addressService.addAddress(address);

        return "redirect:/";
    }

    // Spring Security custom login form (email like username)
    @GetMapping("/login")
    public String getLogin(Model model) {
        addToModelBasicAttributes(model);
        return "login";
    }

    // Spring Security custom logout
    @GetMapping("/logout")
    public String customLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
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
    private String deleteAccount() {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);

        Address address = addressService.getByUserTableID(user);
        addressService.deleteAddress(address);

        List<Review> reviewsByUser = reviewService.getAllReviewsByUser(user);
        reviewService.deleteReviews(reviewsByUser);

        List<OrderTable> orderTables = orderTableService.getAllByUser(user);

        for(OrderTable orderTable : orderTables){
            orderItemService.deleteAll(orderItemService.getAllByOrderTable(orderTable));
        }

        orderTableService.deleteAll(orderTables);

        userTableService.deleteUser(user);
        return "redirect:/";
    }

    @GetMapping("/top-up")
    public String getTopUp(Model model) {
        addToModelBasicAttributes(model);
        return "top-up";
    }

    @PostMapping("/top-up")
    public String postTopUp(@RequestParam("amount") Double amount,
                            @RequestParam("cardNumber") String cardNumber,
                            @RequestParam("expiryDate") String expiryDate,
                            @RequestParam("CVV") String CVV) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        if (cardNumber == null || expiryDate == null || CVV == null) {
            return "redirect:/error-page";
        }
        user.setBalance(user.getBalance() + amount);
        userTableService.editUser(user);
        return "redirect:/account";
    }

    private void addToModelBasicAttributes(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
    }
}