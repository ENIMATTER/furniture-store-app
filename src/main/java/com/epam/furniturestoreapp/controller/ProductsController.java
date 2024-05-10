package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.model.ShopItemDto;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
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
    private List<String> sortOrderList;

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
        sortOrderList = new ArrayList<>(FILTER_LIST);
        Category category = categoryService.findByName(name);
        if (category == null) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
            model.addAttribute("categories", categories);
            return "not-found";
        }
        Page<Product> productPage = productService.getAllProductsByCategoryPage(category, page - 1, size);
        int countOfPages = productPage.getTotalPages();
        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);
        long countOfAllProducts = productPage.getTotalElements();

        List<ShopItemDto> shopItems = pageToListWithoutOutOfStockItems(productPage);

        Map<Material, Boolean> materialMap = new LinkedHashMap<>();
        for(Material m : MATERIAL_LIST){
            materialMap.put(m, false);
        }

        addToModelBasicAttributes(model, shopItems, countOfAllProducts, pages, page, countOfPages, name);
        model.addAttribute("thAction", TH_ACTION_FOR_PRODUCTS_BY_CATEGORY + name);

        model.addAttribute("materialMap", materialMap);
        return "shop";
    }

    @PostMapping("/category/{name}")
    public String postProductsByCategoryId(@PathVariable String name,
                                           @RequestParam(defaultValue = "") String search,
                                           @RequestParam(defaultValue = FIRST_ADDED) String filter,
                                           @RequestParam(required = false) Double from,
                                           @RequestParam(required = false) Double to,
                                           @RequestParam(required = false) Color color,
                                           @RequestParam(required = false) Material[] materials,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "9") int size,
                                           Model model) {
        List<Product> products;
        Category category = categoryService.findByName(name);

        if (category == null) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
            model.addAttribute("categories", categories);
            return "not-found";
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

        List<ShopItemDto> shopItems = new ArrayList<>();
        for (Product p : products) {
            shopItems.add(new ShopItemDto(p));
        }

        Map<Material, Boolean> materialMap = new LinkedHashMap<>();
        if(materials != null){
            boolean[] checked = new boolean[MATERIAL_LIST.size()];
            int j = 0;
            for(Material m1 : MATERIAL_LIST){
                for(Material m2 : materials){
                    if(m1.equals(m2)){
                        checked[j] = true;
                        break;
                    } else {
                        checked[j] = false;
                    }
                }
                j++;
            }
            for(int k = 0; k < MATERIAL_LIST.size(); k++){
                materialMap.put(MATERIAL_LIST.get(k), checked[k]);
            }
        } else {
            for(Material m : MATERIAL_LIST){
                materialMap.put(m, false);
            }
        }

        addToModelBasicAttributes(model, shopItems, countOfAllProducts, pages, page, countOfPages, name);
        model.addAttribute("thAction", TH_ACTION_FOR_PRODUCTS_BY_CATEGORY + name);
        model.addAttribute("search", search);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("color", color);
        model.addAttribute("filter", filter);
        model.addAttribute("materialMap", materialMap);
        return "shop";
    }

    @GetMapping
    public String getProductsPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "9") int size,
                                  Model model) {
        sortOrderList = new ArrayList<>(FILTER_LIST);
        Page<Product> productPage = productService.getAllProductsPage(page - 1, size);
        int countOfPages = productPage.getTotalPages();
        long countOfAllProducts = productPage.getTotalElements();

        Map<Integer, Boolean> pages = makeMapOfPagesNumbers(countOfPages, page);

        List<ShopItemDto> shopItems = pageToListWithoutOutOfStockItems(productPage);

        Map<Material, Boolean> materialMap = new LinkedHashMap<>();
        for(Material m : MATERIAL_LIST){
            materialMap.put(m, false);
        }

        addToModelBasicAttributes(model, shopItems, countOfAllProducts, pages, page, countOfPages, null);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        model.addAttribute("materialMap", materialMap);
        return "shop";
    }

    @PostMapping
    public String postProducts(@RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = FIRST_ADDED) String filter,
                               @RequestParam(required = false) Double from,
                               @RequestParam(required = false) Double to,
                               @RequestParam(required = false) Color color,
                               @RequestParam(required = false) Material[] materials,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "9") int size,
                               Model model) {
        List<Product> products;

        // Search
        if (search == null || search.isBlank()) {
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

        List<ShopItemDto> shopItems = new ArrayList<>();
        for (Product p : products) {
            shopItems.add(new ShopItemDto(p));
        }

        Map<Material, Boolean> materialMap = new LinkedHashMap<>();
        if(materials != null){
            boolean[] checked = new boolean[MATERIAL_LIST.size()];
            int j = 0;
            for(Material m1 : MATERIAL_LIST){
                for(Material m2 : materials){
                    if(m1.equals(m2)){
                        checked[j] = true;
                        break;
                    } else {
                        checked[j] = false;
                    }
                }
                j++;
            }
            for(int k = 0; k < MATERIAL_LIST.size(); k++){
                materialMap.put(MATERIAL_LIST.get(k), checked[k]);
            }
        } else {
            for(Material m : MATERIAL_LIST){
                materialMap.put(m, false);
            }
        }

        addToModelBasicAttributes(model, shopItems, countOfAllProducts, pages, page, countOfPages, null);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        model.addAttribute("search", search);
        model.addAttribute("filter", filter);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("color", color);
        model.addAttribute("materialMap", materialMap);
        return "shop";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable long id, Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);

        Product product = productService.getProductById(id);
        if (product == null) {
            return "not-found";
        }

        Category category = product.getCategoryID();
        List<Review> reviews = reviewService.getAllReviewsByProduct(product);
        String image = Base64.getEncoder().encodeToString(product.getImage());
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        model.addAttribute("image", image);
        return "shop-detail";
    }

    private void addToModelBasicAttributes(Model model, List<ShopItemDto> shopItems, long countOfAllProducts,
                                           Map<Integer, Boolean> pages, int currentPage,
                                           int maxPage, String category) {
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("filterList", sortOrderList);
        model.addAttribute("colorMap", COLOR_MAP);
        model.addAttribute("materialList", MATERIAL_LIST);

        model.addAttribute("shopItems", shopItems);
        model.addAttribute("countOfAllProducts", countOfAllProducts);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("category", category);
    }

    private void filterOrder(List<Product> products, String filter) {
        switch (filter) {
            case BY_RATING -> products.sort(Comparator.comparingDouble(Product::getAverageRating).reversed());
            case FIRST_ADDED -> products.sort(Comparator.comparingLong(Product::getProductID));
            case LAST_ADDED -> products.sort(Comparator.comparingLong(Product::getProductID).reversed());
            case AZ -> products.sort(Comparator.comparing(Product::getProductName));
            case ZA -> products.sort(Comparator.comparing(Product::getProductName).reversed());
            case FROM_HIGHEST_PRICE -> products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
            case FROM_LOWEST_PRICE -> products.sort(Comparator.comparingDouble(Product::getPrice));
        }
        sortOrderList.remove(filter);
        sortOrderList.add(0, filter);
    }

    private List<ShopItemDto> pageToListWithoutOutOfStockItems(Page<Product> productPage) {
        List<ShopItemDto> products = new ArrayList<>();
        for (Product p : productPage) {
            if (p.getStockQuantity() != 0) {
                products.add(new ShopItemDto(p));
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