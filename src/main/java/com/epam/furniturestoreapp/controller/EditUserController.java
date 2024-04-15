package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "edit-user";
    }

    @PutMapping
    public String putEditUser(@ModelAttribute("user") UserTable user) {
        UserTable updatedUser = userTableService.getUserById(user.getUserTableID());
        updatedUser.setFirstname(user.getFirstname());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPhoneNumber(user.getPhoneNumber());
        if (!user.getUserPassword().isBlank()) {
            String codedPassword = new BCryptPasswordEncoder().encode(user.getUserPassword());
            updatedUser.setUserPassword(codedPassword);
        }
        userTableService.editUser(updatedUser);
        return "redirect:/logout";
    }
}

