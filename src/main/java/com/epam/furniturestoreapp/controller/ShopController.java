package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.util.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class ShopController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private static List<String> filterList;
    private String lastSearch = "";

    static {
        filterList = new ArrayList<>();
        filterList.add("Select filter:");
        filterList.add("Last added");
        filterList.add("By rating");
        filterList.add("A-Z");
        filterList.add("Z-A");
        filterList.add("From lowest price");
        filterList.add("From highest price");
    }

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
        model.addAttribute("filterList", filterList);
        model.addAttribute("lastSearch", lastSearch);
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
        model.addAttribute("filterList", filterList);
        model.addAttribute("lastSearch", lastSearch);
        return "shop";
    }

    @PostMapping("/products")
    public String getProductsByName(@ModelAttribute("filterAndSearch") FilterUtil filterAndSearch,
                                    Model model){

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        List<Product> products = productService.getAllProducts();

        if(filterAndSearch.getFilter() == null){
            filterAndSearch.setFilter("Select filter:");
        }

        switch (filterAndSearch.getFilter()) {
            case "By rating" -> products.sort(((o1, o2) -> (int) (o2.getAverageRating() - o1.getAverageRating())));
            case "Last added" -> Collections.reverse(products);
            case "A-Z" -> products.sort((Comparator.comparing(Product::getProductName)));
            case "Z-A" -> products.sort(((o1, o2) -> o2.getProductName().compareTo(o1.getProductName())));
            case "From highest price" -> products.sort(((o1, o2) -> (int) (o2.getPrice() - o1.getPrice())));
            case "From lowest price" -> products.sort((Comparator.comparingDouble(Product::getPrice)));
        }

        filterList.set(0, filterAndSearch.getFilter());

        model.addAttribute("products", products);
        model.addAttribute("filterList", filterList);
        model.addAttribute("lastSearch", lastSearch);

        return "shop";
    }

    @PostMapping("/products/search")
    public String searchProductsByName(@ModelAttribute("name") String name){
        return "redirect:/products/search/" + name;
    }

    @GetMapping("/products/search/{name}")
    public String getSearchProductsByName(Model model, @PathVariable String name){

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        List<Product> products = productService.getAllProductsByName(name);
        lastSearch = name;

        model.addAttribute("products", products);
        model.addAttribute("filterList", filterList);
        model.addAttribute("lastSearch", lastSearch);

        return "shop";
    }

}
