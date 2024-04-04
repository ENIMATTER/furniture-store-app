package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        UserTable user = new UserTable();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUserPassword(userPassword);
        user.setPhoneNumber(phoneNumber);

        userTableService.addUser(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam("email") String email,
                            @RequestParam("userPassword") String userPassword,
                            Model model) {

        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);

        UserTable user = userTableService.getUserByEmail(email);

        if(user == null){
            return "error";
        }
        if(!user.getUserPassword().equals(userPassword)){
            return "error";
        }

        return "redirect:/";
    }
}
