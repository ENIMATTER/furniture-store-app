package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.util.Color;
import com.epam.furniturestoreapp.util.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/products")
@Controller
public class ShopController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private static final List<String> filterList;
    private static final Map<Color, String> colorMap;
    private static final List<Material> materialList;
    private final List<Category> categories;

    private final String thActionForAllProducts = "/products";
    private final String thActionForProductsByCategory = "/products/category/";

    static {
        filterList = new ArrayList<>();
        filterList.add("Last added");
        filterList.add("By rating");
        filterList.add("A-Z");
        filterList.add("Z-A");
        filterList.add("From lowest price");
        filterList.add("From highest price");
    }

    static {
        colorMap = new HashMap<>();
        colorMap.put(Color.White, "background-color:#e0dfdf;");
        colorMap.put(Color.Blue, "background-color:#0b5fb5;");
        colorMap.put(Color.Green, "background-color:#00a651;");
        colorMap.put(Color.Yellow, "background-color:#f7ff01;");
        colorMap.put(Color.Red, "background-color:#ff0000;");
        colorMap.put(Color.Black, "background-color:#000000;");
        colorMap.put(Color.Brown, "background-color:#65270c;");
    }

    static {
        materialList = new ArrayList<>();
        materialList.add(Material.Wood);
        materialList.add(Material.Metal);
        materialList.add(Material.Glass);
        materialList.add(Material.Leather);
        materialList.add(Material.Textile);
    }

    @Autowired
    public ShopController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;

        categories = categoryService.findAll();
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategoryId(@PathVariable long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "not-found";
        }
        List<Product> products = productService.getAllProductsByCategory(category);
        addToModelBasicAttributes(model, products);
        model.addAttribute("thAction", thActionForProductsByCategory + id);
        model.addAttribute("categoryId", id);
        return "shop";
    }

    @PostMapping("/category/{id}")
    public String postProductsByCategoryId(@PathVariable long id,
                                           @RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "filter", defaultValue = "Last added") String filter,
                                           @RequestParam(value = "from", required = false) Double from,
                                           @RequestParam(value = "to", required = false) Double to,
                                           @RequestParam(value = "color", required = false) Color color,
                                           @RequestParam(value = "materials", required = false) Material[] materials,
                                           Model model) {
        List<Product> products;
        Category category = categoryService.findById(id);
        if (category == null) {
            return "not-found";
        }

        // Search
        if (search == null || search.isEmpty()) {
            products = productService.getAllProductsByCategory(category);
        } else {
            products = productService.getAllByCategoryIDAndProductNameContaining(category, search);
        }

        // Filter by price, color and material
        if(from != null || to != null || color != null || materials != null){
            products = productService.getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(category, from, to, color, materials);
        }

        // Filter order
        filterOrder(products, filter);

        addToModelBasicAttributes(model, products);
        model.addAttribute("thAction", thActionForProductsByCategory + id);
        model.addAttribute("categoryId", id);
        return "shop";
    }

    @GetMapping
    public String getProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        addToModelBasicAttributes(model, products);
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
        if(from != null || to != null || color != null || materials != null){
            products = productService.getAllByPriceBetweenAndColorAndMaterial(from, to, color, materials);
        }

        // Filter order
        filterOrder(products, filter);

        addToModelBasicAttributes(model, products);
        model.addAttribute("thAction", thActionForAllProducts);
        return "shop";
    }

    private void addToModelBasicAttributes(Model model, List<Product> products){
        model.addAttribute("categories", categories);
        model.addAttribute("filterList", filterList);
        model.addAttribute("colorMap", colorMap);
        model.addAttribute("materialList", materialList);
        model.addAttribute("products", products);
    }

    private void filterOrder(List<Product> products, String filter){
        switch (filter) {
            case "By rating" -> products.sort(((o1, o2) -> (int) (o2.getAverageRating() - o1.getAverageRating())));
            case "Last added" -> Collections.reverse(products);
            case "A-Z" -> products.sort((Comparator.comparing(Product::getProductName)));
            case "Z-A" -> products.sort(((o1, o2) -> o2.getProductName().compareTo(o1.getProductName())));
            case "From highest price" -> products.sort(((o1, o2) -> (int) (o2.getPrice() - o1.getPrice())));
            case "From lowest price" -> products.sort((Comparator.comparingDouble(Product::getPrice)));
        }
        filterList.remove(filter);
        filterList.add(0, filter);
    }
}