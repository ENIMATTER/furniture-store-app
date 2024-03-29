package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ShopController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public ShopController(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getAllProducts(Model model){
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.getAllProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/products/{id}")
    public String getProductsByCategoryId(@PathVariable long id, Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        Category category = categoryService.findById(id);
        if(category == null){
            return "not-found";
        }
        List<Product> products = productService.getAllProductsByCategory(category);
        model.addAttribute("products", products);
        return "shop";
    }
}
