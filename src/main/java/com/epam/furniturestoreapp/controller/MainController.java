package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
public class MainController {

    private final CategoryService categoryService;
    private final List<Category> categories;

    @Autowired
    public MainController(CategoryService categoryService) {
        this.categoryService = categoryService;
        categories = categoryService.findAll();
    }

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "index";
    }

    @GetMapping("/not-found")
    public String notFound(Model model){
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "not-found";
    }

    ////////////////////////////////////////

    @GetMapping("/account")
    public String account(){
        return "account";
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(){
        return "checkout";
    }

    @GetMapping("/edit-address")
    public String editAddress(){
        return "edit-address";
    }

    @GetMapping("/edit-user")
    public String editUser(){
        return "edit-user";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

}
