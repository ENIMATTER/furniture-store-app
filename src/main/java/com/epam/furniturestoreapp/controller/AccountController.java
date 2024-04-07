package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.AddressService;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final List<Category> categories;

    public AccountController(CategoryService categoryService, UserTableService userTableService,
                             AddressService addressService) {
        this.userTableService = userTableService;
        this.addressService = addressService;
        this.categories = categoryService.findAll();
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
            return "error-page";
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
        addToModelBasicAttributes(model);
        return "account";
    }

    @DeleteMapping("/account")
    private String deleteAccount(Model model){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(email);
        Address address = addressService.getByUserTableID(user);
        addressService.deleteAddress(address);
        userTableService.deleteUser(user);
        addToModelBasicAttributes(model);
        return "index";
    }

    @GetMapping("/edit-address")
    public String getEditAddress(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(email);
        Address address = addressService.getByUserTableID(user);
        model.addAttribute("address", address);
        addToModelBasicAttributes(model);
        return "edit-address";
    }

    @PutMapping("/edit-address")
    public String putEditAddress(@RequestParam("addressID") Long addressID,
                                  @RequestParam("street") String street,
                                  @RequestParam("city") String city,
                                  @RequestParam("state") String state,
                                  @RequestParam("country") String country,
                                  @RequestParam("zipCode") String zipCode,
                                  Model model) {
        Address address = addressService.getAddressById(addressID);
        if(address == null){
            return "error-page";
        }
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zipCode);
        addressService.editAddress(address);
        model.addAttribute("address", address);
        addToModelBasicAttributes(model);
        return "edit-address";
    }

    @GetMapping("/edit-user")
    public String getEditUser(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(email);
        model.addAttribute("user", user);
        addToModelBasicAttributes(model);
        return "edit-user";
    }

    @PutMapping("/edit-user")
    public String putEditUser(@RequestParam("userTableID") Long userTableID,
                               @RequestParam("firstname") String firstname,
                               @RequestParam("lastname") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("password") String password,
                               Model model) {
        UserTable user = userTableService.getUserById(userTableID);
        if(user == null){
            return "error-page";
        }
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        if (!password.isBlank()) {
            String codedPassword = new BCryptPasswordEncoder().encode(password);
            user.setUserPassword(codedPassword);
        }
        userTableService.editUser(user);
        model.addAttribute("user", user);
        addToModelBasicAttributes(model);
        return "edit-user";
    }

    private void addToModelBasicAttributes(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
    }

}