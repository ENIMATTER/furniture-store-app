package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.CartItem;
import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/cart")
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

    @GetMapping
    public String getCart(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItems = cartItemService.getAllItemsByUser(user);
        Map<CartItem, Double> totals = new HashMap<>();
        cartItems.forEach(cartItem -> totals.put(cartItem, cartItem.getQuantity() * cartItem.getProductID().getPrice()));
        double allTotal = 0;
        for (Double d : totals.values()) {
            allTotal += d;
        }
        model.addAttribute("allTotal", allTotal);
        model.addAttribute("totals", totals);
        model.addAttribute("cartItems", cartItems);
        addToModelBasicAttributes(model);
        return "cart";
    }

    @PostMapping
    public String addToCartItem(@RequestParam("cartProductID") Long cartProductID,
                                @RequestParam("cartQuantity") Integer cartQuantity) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Product product = productService.getProductById(cartProductID);
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItemsByUser = cartItemService.getAllItemsByUser(user);
        List<CartItem> allCartItems = cartItemService.getAll();
        boolean createNewItem = true;
        for (CartItem cartItem : cartItemsByUser) {
            if (cartItem.getProductID().getProductID() == product.getProductID()) {
                cartItem.setQuantity(cartItem.getQuantity() + cartQuantity);
                createNewItem = false;
                break;
            }
        }
        if (createNewItem) {
            CartItem newCartItem = new CartItem();
            newCartItem.setUserTableID(user);
            newCartItem.setProductID(product);
            newCartItem.setQuantity(cartQuantity);
            allCartItems.add(newCartItem);
        }
        cartItemService.saveAll(allCartItems);
        return "redirect:/cart";
    }

    @DeleteMapping
    public String deleteCartItem(@RequestParam("cartItemID") Long cartItemID) {
        cartItemService.deleteById(cartItemID);
        return "redirect:/cart";
    }

    @PutMapping
    public String changeQuantityCartItem(@RequestParam("cartItemID") Long cartItemID,
                                         @RequestParam("cartQuantity") Integer cartQuantity){
        if(cartQuantity == 0){
            cartItemService.deleteById(cartItemID);
        } else {
            CartItem cartItem = cartItemService.getById(cartItemID);
            if(cartItem != null) {
                cartItem.setQuantity(cartQuantity);
                cartItemService.save(cartItem);
            }
        }
        return "redirect:/cart";
    }

    private void addToModelBasicAttributes(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
    }
}
