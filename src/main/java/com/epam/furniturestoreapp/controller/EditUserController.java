package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.emailUsername;
import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/edit-user")
public class EditUserController {
    private final UserTableService userTableService;
    private final List<Category> categories;

    @Autowired
    public EditUserController(UserTableService userTableService, CategoryService categoryService) {
        this.userTableService = userTableService;
        this.categories = categoryService.findAll();
    }

    @GetMapping
    public String getEditUser(Model model) {
        UserTable user = userTableService.getUserByEmail(emailUsername);
        model.addAttribute("user", user);
        addToModelBasicAttributes(model);
        return "edit-user";
    }

    @PutMapping
    public String putEditUser(@RequestParam("userTableID") Long userTableID,
                              @RequestParam("firstname") String firstname,
                              @RequestParam("lastname") String lastname,
                              @RequestParam("email") String email,
                              @RequestParam("phoneNumber") String phoneNumber,
                              @RequestParam("password") String password,
                              Model model) {
        UserTable user = userTableService.getUserById(userTableID);
        if (user == null) {
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
