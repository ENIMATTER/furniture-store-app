package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainPageController {
    private final CategoryService categoryService;
    private final String thActionForAllProducts = "/products";

    @Autowired
    public MainPageController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "index";
    }
}
