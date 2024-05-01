package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.EditUserDto;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
@RequestMapping("/edit-user")
public class EditUserController {
    private final UserTableService userTableService;
    private final CategoryService categoryService;

    @Autowired
    public EditUserController(UserTableService userTableService, CategoryService categoryService) {
        this.userTableService = userTableService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getEditUser(Model model) {
        addToModelBasicAttributes(model);
        return "edit-user";
    }

    @PutMapping
    public String putEditUser(HttpServletRequest request, HttpServletResponse response,
                              @Valid @ModelAttribute("user") EditUserDto user, BindingResult result,
                              Model model) {
        addToModelBasicAttributes(model);
        if(result.hasErrors()){
            model.addAttribute("fail", true);
            return "edit-user";
        }
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userTableService.existsByEmail(user.getEmail()) && !user.getEmail().equals(emailUsername)){
            model.addAttribute("exist", true);
            return "edit-user";
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "login";
    }

    private void addToModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}