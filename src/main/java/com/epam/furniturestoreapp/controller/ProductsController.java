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

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;
import static com.epam.furniturestoreapp.util.StaticVariables.thActionForProductsByCategory;

@RequestMapping("/products")
@Controller
public class ProductsController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;

    private static final List<String> filterList;
    private static final Map<Color, String> colorMap;
    private static final List<Material> materialList;
    private final List<Category> categories;

    private static final String lastAdded = "Last added";
    private static final String byRating = "By rating";
    private static final String AZ = "A-Z";
    private static final String ZA = "Z-A";
    private static final String fromLowestPrice = "From lowest price";
    private static final String fromHighestPrice = "From highest price";

    static {
        filterList = new ArrayList<>();
        filterList.add(lastAdded);
        filterList.add(byRating);
        filterList.add(AZ);
        filterList.add(ZA);
        filterList.add(fromLowestPrice);
        filterList.add(fromHighestPrice);
    }

    static {
        colorMap = new LinkedHashMap<>();
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
    public ProductsController(CategoryService categoryService, ProductService productService,
                              ReviewService reviewService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
        categories = categoryService.findAll();
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategoryId(@PathVariable long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/not-found";
        }
        List<Product> products = productService.getAllProductsByCategory(category);
        products.removeIf(product -> product.getStockQuantity() == 0);
        addToModelBasicAttributes(model);
        model.addAttribute("products", products);
        model.addAttribute("thAction", thActionForProductsByCategory + id);
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

        model.addAttribute("thAction", thActionForProductsByCategory + id);
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
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        return "shop-detail";
    }

    private void addToModelBasicAttributes(Model model) {
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