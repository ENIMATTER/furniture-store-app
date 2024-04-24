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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartItemService cartItemService;
    private final UserTableService userTableService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public CartController(CartItemService cartItemService, UserTableService userTableService,
                          ProductService productService, CategoryService categoryService) {
        this.cartItemService = cartItemService;
        this.userTableService = userTableService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getCart(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItems = user.getCartItems();
        Map<CartItem, BigDecimal> totals = new HashMap<>();
        user.getCartItems().forEach(cartItem -> totals.put(cartItem, BigDecimal
                        .valueOf(cartItem.getQuantity() * cartItem.getProductID().getPrice())
                        .setScale(2, RoundingMode.HALF_UP)));
        BigDecimal allTotal = BigDecimal.ZERO;
        for (BigDecimal d : totals.values()) {
            allTotal = allTotal.add(d);
        }
        allTotal = allTotal.setScale(2, RoundingMode.HALF_UP);
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
        List<CartItem> cartItemsByUser = user.getCartItems();
        List<CartItem> allCartItems = cartItemService.getAll();

        boolean createNewItem = true;
        for (CartItem cartItem : cartItemsByUser) {
            if (cartItem.getProductID().getProductID().equals(product.getProductID())) {
                if(cartItem.getProductID().getStockQuantity() < cartItem.getQuantity() + cartQuantity){
                    return "redirect:/cart?lowquantity";
                }
                cartItem.setQuantity(cartItem.getQuantity() + cartQuantity);
                createNewItem = false;
                break;
            }
        }
        if (createNewItem) {
            if(product.getStockQuantity() < cartQuantity){
                return "redirect:/cart?lowquantity";
            }
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
                if(cartItem.getProductID().getStockQuantity() < cartQuantity){
                    return "redirect:/cart?lowquantity";
                }
                cartItem.setQuantity(cartQuantity);
                cartItemService.save(cartItem);
            }
        }
        return "redirect:/cart";
    }

    private void addToModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
