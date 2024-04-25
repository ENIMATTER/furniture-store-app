package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.UserTableService;
import com.epam.furniturestoreapp.model.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
@RequestMapping("/users-admin")
public class AdminUserController {
    private final UserTableService userTableService;

    @Autowired
    public AdminUserController(UserTableService userTableService) {
        this.userTableService = userTableService;
    }

    @GetMapping
    public String getUsersAdmin(Model model) {
        List<UserTable> users = userTableService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        return "users-admin";
    }

    @PostMapping
    public String addUserAdmin(@Valid @ModelAttribute("userUtil") UserDto userUtil,
                               BindingResult result) {
        if(result.hasErrors()){
            return "redirect:/users-admin?fail";
        }
        String codedPassword = new BCryptPasswordEncoder().encode(userUtil.getPassword());
        UserTable userTable = new UserTable(userUtil.getFirstname(), userUtil.getLastname(), userUtil.getEmail(),
                codedPassword, userUtil.getPhone(), userUtil.getBalance(), userUtil.getRoles());
        userTableService.addUser(userTable);
        return "redirect:/users-admin";
    }

    @PutMapping
    public String editUserAdmin(@Valid @ModelAttribute("user") UserTable user,
                                BindingResult result) {
        if(result.hasErrors()){
            return "redirect:/users-admin?fail";
        }
        UserTable userTable = userTableService.getUserById(user.getUserTableID());
        if(userTableService.existsByEmail(user.getEmail()) && !userTable.getEmail().equals(user.getEmail())){
            return "redirect:/users-admin?exist";
        }
        if(user.getPhoneNumber().length() != 9){
            return "redirect:/users-admin?phone";
        }
        for(char c : user.getPhoneNumber().toCharArray()){
            if(!Character.isDigit(c)){
                return "redirect:/users-admin?phone";
            }
        }
        userTable.setFirstname(user.getFirstname());
        userTable.setLastname(user.getLastname());
        userTable.setEmail(user.getEmail());
        userTable.setPhoneNumber(user.getPhoneNumber());
        userTable.setBalance(user.getBalance());
        userTable.setRoles(user.getRoles());
        if (!user.getUserPassword().isBlank()) {
            String codedPassword = new BCryptPasswordEncoder().encode(user.getUserPassword());
            userTable.setUserPassword(codedPassword);
        }
        userTableService.editUser(userTable);
        return "redirect:/users-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUserAdmin(@PathVariable Long id) {
        if(userTableService.existsById(id)){
            userTableService.deleteUserById(id);
        }
        return "redirect:/users-admin";
    }
}
