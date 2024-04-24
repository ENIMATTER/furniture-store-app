package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.service.*;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.epam.furniturestoreapp.model.StaticVariables.*;

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
    public String getProductsByCategoryId(@PathVariable String name,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "9") int size,
                                          Model model) {
        Category category = categoryService.findByName(name);
        if (category == null) {
            return "redirect:/not-found";
        }
        Page<Product> productPage = productService.getAllProductsByCategoryPage(category, page - 1, size);
        int countOfPages = productPage.getTotalPages();
        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);
        List<Product> products = pageToListWithoutOutOfStockItems(productPage);
        long countOfAllProducts = productPage.getTotalElements();

        addToModelBasicAttributes(model, products, countOfAllProducts, pages, page, countOfPages, name);
        model.addAttribute("thAction", TH_ACTION_FOR_PRODUCTS_BY_CATEGORY + name);
        return "shop";
    }

    @PostMapping("/category/{name}")
    public String postProductsByCategoryId(@PathVariable String name,
                                           @RequestParam(defaultValue = "") String search,
                                           @RequestParam(value = "filter", defaultValue = "Last added") String filter,
                                           @RequestParam(value = "from", required = false) Double from,
                                           @RequestParam(value = "to", required = false) Double to,
                                           @RequestParam(value = "color", required = false) Color color,
                                           @RequestParam(value = "materials", required = false) Material[] materials,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "9") int size,
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

        // Page
        long countOfAllProducts = products.size();
        Pageable pageRequest = createPageRequestUsing(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), products.size());

        products = products.subList(start, end);

        int countOfPages = (int) Math.ceil((double) countOfAllProducts / size);

        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);

        addToModelBasicAttributes(model, products, countOfAllProducts, pages, page, countOfPages, name);
        model.addAttribute("search", search);
        model.addAttribute("thAction", TH_ACTION_FOR_PRODUCTS_BY_CATEGORY + name);
        return "shop";
    }

    @GetMapping
    public String getProductsPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "9") int size,
                                  Model model) {
        Page<Product> productPage = productService.getAllProductsPage(page - 1, size);
        int countOfPages = productPage.getTotalPages();
        long countOfAllProducts = productPage.getTotalElements();

        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);

        List<Product> products = pageToListWithoutOutOfStockItems(productPage);

        addToModelBasicAttributes(model, products, countOfAllProducts, pages, page, countOfPages, null);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        return "shop";
    }

    @PostMapping
    public String postProducts(@RequestParam(defaultValue = "") String search,
                               @RequestParam(value = "filter", defaultValue = "Last added") String filter,
                               @RequestParam(value = "from", required = false) Double from,
                               @RequestParam(value = "to", required = false) Double to,
                               @RequestParam(value = "color", required = false) Color color,
                               @RequestParam(value = "materials", required = false) Material[] materials,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "9") int size,
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

        // Page
        long countOfAllProducts = products.size();
        Pageable pageRequest = createPageRequestUsing(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), products.size());

        products = products.subList(start, end);

        int countOfPages = (int) Math.ceil((double) countOfAllProducts / size);

        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);

        addToModelBasicAttributes(model, products, countOfAllProducts, pages, page, countOfPages, null);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        model.addAttribute("search", search);
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
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        return "shop-detail";
    }

    private void addToModelBasicAttributes(Model model, List<Product> products, long countOfAllProducts,
                                           Map<Integer, Boolean> pages, int currentPage, int maxPage,
                                           String category) {
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("filterList", FILTER_LIST);
        model.addAttribute("colorMap", COLOR_MAP);
        model.addAttribute("materialList", MATERIAL_LIST);

        model.addAttribute("products", products);
        model.addAttribute("countOfAllProducts", countOfAllProducts);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("category", category);
    }

    private void filterOrder(List<Product> products, String filter) {
        switch (filter) {
            case BY_RATING -> products.sort(Comparator.comparingDouble(Product::getAverageRating).reversed());
            case LAST_ADDED -> Collections.reverse(products);
            case AZ -> products.sort(Comparator.comparing(Product::getProductName));
            case ZA -> products.sort(Comparator.comparing(Product::getProductName).reversed());
            case FROM_HIGHEST_PRICE -> products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
            case FROM_LOWEST_PRICE -> products.sort(Comparator.comparingDouble(Product::getPrice));
        }
        FILTER_LIST.remove(filter);
        FILTER_LIST.add(0, filter);
    }

    private List<Product> pageToListWithoutOutOfStockItems(Page<Product> productPage) {
        List<Product> products = new ArrayList<>();
        for (Product p : productPage) {
            if (p.getStockQuantity() != 0) {
                products.add(p);
            }
        }
        return products;
    }

    private Map<Integer, Boolean> makeMapOfPagesNumbers(int countOfPages, int currentPage) {
        Map<Integer, Boolean> pages = new LinkedHashMap<>();
        for (int i = 1; i <= countOfPages; i++) {
            if (i == currentPage) {
                pages.put(i, true);
            } else {
                pages.put(i, false);
            }
        }
        return pages;
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }
}