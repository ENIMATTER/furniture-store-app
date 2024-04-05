package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
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
public class UserController {
    private final CategoryService categoryService;
    private final UserTableService userTableService;

    private final List<Category> categories;

    public UserController(CategoryService categoryService, UserTableService userTableService) {
        this.categoryService = categoryService;
        this.userTableService = userTableService;
        this.categories = categoryService.findAll();
    }

    @GetMapping("/signup")
    public String getSignup(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignup(@RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("email") String email,
                             @RequestParam("userPassword") String userPassword,
                             @RequestParam("phoneNumber") String phoneNumber,
                             Model model) {

        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);

        if(userTableService.existsByEmail(email)){
            return "error";
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

        return "redirect:/";
    }

    // Spring Security custom login form (email like username)
    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "login";
    }

    // Spring Security custom logout
    @GetMapping("/logout")
    public String customLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

}
