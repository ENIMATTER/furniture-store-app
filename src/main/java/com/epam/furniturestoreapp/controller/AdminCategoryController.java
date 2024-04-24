package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

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
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        return "categories-admin";
    }

    @PutMapping
    public String editCategoryAdmin(@Valid @ModelAttribute("categoryEdit") Category categoryEdit,
                                    BindingResult result){
        if (result.hasErrors()) {
            return "redirect:/categories-admin?fail";
        }
        if(categoryService.existByName(categoryEdit.getCategoryName())){
            return "redirect:/categories-admin?exist";
        }
        Category category = categoryService.findById(categoryEdit.getCategoryID());
        category.setCategoryName(categoryEdit.getCategoryName());
        categoryService.save(category);
        return "redirect:/categories-admin";
    }

    @PostMapping
    public String addCategoryAdmin(@RequestParam("categoryName") String categoryName){
        if(categoryService.existByName(categoryName)){
            return "redirect:/categories-admin?exist";
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryService.save(category);
        return "redirect:/categories-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryAdmin(@PathVariable Long id){
        if(categoryService.existById(id)){
            categoryService.deleteByID(id);
        }
        return "redirect:/categories-admin";
    }
}
