package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserTableController {
    private final UserTableService userTableService;

    @Autowired
    public UserTableController(UserTableService userTableService){
        this.userTableService = userTableService;
    }

    @GetMapping("/add")
    public String getAddUser(Model model) {
        UserTable user = new UserTable();
        model.addAttribute("user", user);
        return "";
    }

    @PostMapping("/add")
    public String postAddUser(@Valid @ModelAttribute("address") UserTable user,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "";
        }
        userTableService.addUser(user);
        return "";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable(value = "id") long id, Model model) {
        if (!userTableService.existsById(id)) {
            return "";
        }
        UserTable user = userTableService.findById(id);
        model.addAttribute("user", user);
        return "";
    }

    @GetMapping("/{id}/edit")
    public String getEditUser(@PathVariable(value = "id") long id, Model model) {
        if (!userTableService.existsById(id)) {
            return "";
        }
        UserTable user = userTableService.findById(id);
        model.addAttribute("user", user);
        return "";
    }

    @PutMapping("/{id}/edit")
    public String putEditUser(@PathVariable(value = "id") long id,
                                 @Valid @ModelAttribute("address") UserTable user,
                                 BindingResult bindingResult) {
        if (!userTableService.existsById(id)) {
            return "";
        }
        if (bindingResult.hasErrors()) {
            return "";
        }
        UserTable newUser = userTableService.findById(id);

        userTableService.updateUser(newUser);
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable(value = "id") long id) {
        if (!userTableService.existsById(id)) {
            return "";
        }
        userTableService.deleteById(id);
        return "";
    }
}
