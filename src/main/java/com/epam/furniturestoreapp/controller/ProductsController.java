package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.util.Color;
import com.epam.furniturestoreapp.util.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.epam.furniturestoreapp.util.StaticVariables.*;

@RequestMapping("/products")
@Controller
public class ProductsController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;

    @Autowired
    public ProductsController(CategoryService categoryService, ProductService productService,
                              ReviewService reviewService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/category/{name}")
    public String getProductsByCategoryId(@PathVariable String name, Model model) {
        Category category = categoryService.findByName(name);
        if (category == null) {
            return "redirect:/not-found";
        }
        List<Product> products = productService.getAllProductsByCategory(category);
        products.removeIf(product -> product.getStockQuantity() == 0);
        addToModelBasicAttributes(model);
        model.addAttribute("products", products);
        model.addAttribute("thAction", thActionForProductsByCategory + name);
        return "shop";
    }

    @PostMapping("/category/{name}")
    public String postProductsByCategoryId(@PathVariable String name,
                                           @RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "filter", defaultValue = "Last added") String filter,
                                           @RequestParam(value = "from", required = false) Double from,
                                           @RequestParam(value = "to", required = false) Double to,
                                           @RequestParam(value = "color", required = false) Color color,
                                           @RequestParam(value = "materials", required = false) Material[] materials,
                                           Model model) {
        List<Product> products;
        Category category = categoryService.findByName(name);

        if (category == null) {
            return "redirect:/not-found";
        }

        // Search
        if (search == null || search.isEmpty()) {
            products = productService.getAllProductsByCategory(category);
        } else {
            products = productService.getAllByCategoryIDAndProductNameContaining(category, search);
        }

        // Filter by price, color and material
        if (from != null || to != null || color != null || materials != null) {
            products = productService.getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(category, from, to, color, materials);
        }

        // Filter order
        filterOrder(products, filter);

        products.removeIf(product -> product.getStockQuantity() == 0);

        model.addAttribute("thAction", thActionForProductsByCategory + name);
        model.addAttribute("products", products);
        addToModelBasicAttributes(model);
        return "shop";
    }

    @GetMapping
    public String getProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        products.removeIf(product -> product.getStockQuantity() == 0);
        addToModelBasicAttributes(model);
        model.addAttribute("products", products);
        model.addAttribute("thAction", thActionForAllProducts);
        return "shop";
    }

    @PostMapping
    public String postProducts(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "filter", defaultValue = "Last added") String filter,
                               @RequestParam(value = "from", required = false) Double from,
                               @RequestParam(value = "to", required = false) Double to,
                               @RequestParam(value = "color", required = false) Color color,
                               @RequestParam(value = "materials", required = false) Material[] materials,
                               Model model) {
        List<Product> products;

        // Search
        if (search == null || search.isEmpty()) {
            products = productService.getAllProducts();
        } else {
            products = productService.getAllByProductNameContaining(search);
        }

        // Filter by price, color and material
        if (from != null || to != null || color != null || materials != null) {
            products = productService.getAllByPriceBetweenAndColorAndMaterial(from, to, color, materials);
        }

        // Filter order
        filterOrder(products, filter);

        products.removeIf(product -> product.getStockQuantity() == 0);

        addToModelBasicAttributes(model);
        model.addAttribute("products", products);
        model.addAttribute("thAction", thActionForAllProducts);
        return "shop";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/not-found";
        }
        Category category = product.getCategoryID();
        List<Review> reviews = reviewService.getAllReviewsByProduct(product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        return "shop-detail";
    }

    private void addToModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("filterList", filterList);
        model.addAttribute("colorMap", colorMap);
        model.addAttribute("materialList", materialList);
    }

    private void filterOrder(List<Product> products, String filter) {
        switch (filter) {
            case byRating -> products.sort(Comparator.comparingDouble(Product::getAverageRating).reversed());
            case lastAdded -> Collections.reverse(products);
            case AZ -> products.sort(Comparator.comparing(Product::getProductName));
            case ZA -> products.sort(Comparator.comparing(Product::getProductName).reversed());
            case fromHighestPrice -> products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
            case fromLowestPrice -> products.sort(Comparator.comparingDouble(Product::getPrice));
        }
        filterList.remove(filter);
        filterList.add(0, filter);
    }
}