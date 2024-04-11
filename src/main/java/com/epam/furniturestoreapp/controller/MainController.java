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

    private final List<Category> categories;

    @Autowired
    public MainController(CategoryService categoryService) {
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

    @GetMapping("/error-page")
    public String error(Model model){
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "error-page";
    }
}
