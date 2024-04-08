package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.CartItem;
import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

import static com.epam.furniturestoreapp.util.StaticVariables.emailUsername;
import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
public class CartController {
    private final CartItemService cartItemService;
    private final UserTableService userTableService;
    private final ProductService productService;
    private final List<Category> categories;

    @Autowired
    public CartController(CartItemService cartItemService, UserTableService userTableService,
                          ProductService productService, CategoryService categoryService) {
        this.cartItemService = cartItemService;
        this.userTableService = userTableService;
        this.productService = productService;
        categories = categoryService.findAll();
    }

    @GetMapping("/cart")
    public String cart(Model model){
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItems = cartItemService.getAllItemsByUser(user);
        Map<CartItem, Double> totals = new HashMap<>();
        cartItems.forEach(cartItem -> {
            totals.put(cartItem, cartItem.getQuantity() * cartItem.getProductID().getPrice());
        });
        double allTotal = 0;
        for(Double d : totals.values()){
            allTotal += d;
        }
        model.addAttribute("allTotal", allTotal);
        model.addAttribute("totals", totals);
        model.addAttribute("cartItems", cartItems);
        addToModelBasicAttributes(model);
        return "cart";
    }

    private void addToModelBasicAttributes(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
    }
}
