package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "";
    }

    @GetMapping("/add")
    public String getAddCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "";
    }

    @PostMapping("/add")
    public String postAddCategory(@Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "";
        }
        categoryService.addCategory(category);
        return "";
    }

    @GetMapping("/{id}")
    public String getCategory(@PathVariable(value = "id") long id, Model model) {
        if (!categoryService.existsById(id)) {
            return "";
        }
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "";
    }

    @GetMapping("/{id}/edit")
    public String getEditCategory(@PathVariable(value = "id") long id, Model model) {
        if (!categoryService.existsById(id)) {
            return "";
        }
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "";
    }

    @PutMapping("/{id}/edit")
    public String putEditCategory(@PathVariable(value = "id") long id,
                                  @Valid @ModelAttribute("category") Category category,
                                  BindingResult bindingResult) {
        if (!categoryService.existsById(id)) {
            return "";
        }
        if (bindingResult.hasErrors()) {
            return "";
        }
        Category newCategory = categoryService.findById(id);
        newCategory.setCategoryName(category.getCategoryName());
        categoryService.updateCategory(newCategory);
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable(value = "id") long id) {
        if (!categoryService.existsById(id)) {
            return "";
        }
        categoryService.deleteById(id);
        return "";
    }
}
