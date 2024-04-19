package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/categories-admin")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCategoriesAdmin(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "categories-admin";
    }

    @PutMapping
    public String editCategoryAdmin(@ModelAttribute("categoryEdit") Category categoryEdit){
        if(categoryService.existByName(categoryEdit.getCategoryName())){
            return "/categories-admin?exist";
        }
        Category category = categoryService.findById(categoryEdit.getCategoryID());
        category.setCategoryName(categoryEdit.getCategoryName());
        categoryService.save(category);
        return "redirect:/categories-admin";
    }

    @PostMapping
    public String addCategoryAdmin(@RequestParam("categoryName") String categoryName){
        if(categoryService.existByName(categoryName)){
            return "/categories-admin?exist";
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryService.save(category);
        return "redirect:/categories-admin";
    }

    @DeleteMapping
    public String deleteCategoryAdmin(@RequestParam("categoryID") Long categoryID){
        categoryService.deleteByID(categoryID);
        return "redirect:/categories-admin";
    }
}
