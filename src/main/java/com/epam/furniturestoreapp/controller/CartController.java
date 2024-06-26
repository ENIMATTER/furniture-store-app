package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.CartItem;
import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.CartItemDto;
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
        addToModelAllAttributes(model);
        return "cart";
    }

    @PostMapping
    public String addToCartItem(@RequestParam("cartProductID") Long cartProductID,
                                @RequestParam("cartQuantity") Integer cartQuantity, Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Product product = productService.getProductById(cartProductID);
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItemsByUser = cartItemService.getAllItemsByUser(user);
        List<CartItem> allCartItems = cartItemService.getAll();

        boolean createNewItem = true;
        for (CartItem cartItem : cartItemsByUser) {
            if (cartItem.getProductID().getProductID().equals(product.getProductID())) {
                if(cartItem.getProductID().getStockQuantity() < cartItem.getQuantity() + cartQuantity){
                    addToModelAllAttributes(model);
                    model.addAttribute("lowquantity", true);
                    return "cart";
                }
                cartItem.setQuantity(cartItem.getQuantity() + cartQuantity);
                createNewItem = false;
                break;
            }
        }
        if (createNewItem) {
            if(product.getStockQuantity() < cartQuantity){
                addToModelAllAttributes(model);
                model.addAttribute("lowquantity", true);
                return "cart";
            }
            CartItem newCartItem = new CartItem();
            newCartItem.setUserTableID(user);
            newCartItem.setProductID(product);
            newCartItem.setQuantity(cartQuantity);
            allCartItems.add(newCartItem);
        }
        cartItemService.saveAll(allCartItems);

        addToModelAllAttributes(model);
        return "cart";
    }

    @DeleteMapping
    public String deleteCartItem(@RequestParam("cartItemID") Long cartItemID, Model model) {
        cartItemService.deleteById(cartItemID);
        addToModelAllAttributes(model);
        return "cart";
    }

    @PutMapping
    public String changeQuantityCartItem(@RequestParam("cartItemID") Long cartItemID,
                                         @RequestParam("cartQuantity") Integer cartQuantity, Model model){
        if(cartQuantity == 0){
            cartItemService.deleteById(cartItemID);
        } else {
            CartItem cartItem = cartItemService.getById(cartItemID);
            if(cartItem != null) {
                if(cartItem.getProductID().getStockQuantity() < cartQuantity){
                    addToModelAllAttributes(model);
                    model.addAttribute("lowquantity", true);
                    return "cart";
                }
                cartItem.setQuantity(cartQuantity);
                cartItemService.save(cartItem);
            }
        }
        addToModelAllAttributes(model);
        return "cart";
    }

    private void addToModelBasicAttributes(Model model, BigDecimal allTotal, List<CartItemDto> cartItemDtos) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("allTotal", allTotal);
        model.addAttribute("cartItemDtos", cartItemDtos);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }

    private List<CartItemDto> getCartItemsDtoFromCartItems(List<CartItem> cartItems){
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem c : cartItems){
            cartItemDtos.add(new CartItemDto(c));
        }
        return cartItemDtos;
    }

    private BigDecimal getAllTotal(List<CartItemDto> cartItemDtos) {
        BigDecimal allTotal = BigDecimal.ZERO;
        for (CartItemDto c : cartItemDtos) {
            allTotal = allTotal.add(c.getTotals());
        }
        allTotal = allTotal.setScale(2, RoundingMode.HALF_UP);
        return allTotal;
    }

    private void addToModelAllAttributes(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItems = cartItemService.getAllItemsByUser(user);
        List<CartItemDto> cartItemDtos = getCartItemsDtoFromCartItems(cartItems);
        BigDecimal allTotal = getAllTotal(cartItemDtos);
        addToModelBasicAttributes(model, allTotal, cartItemDtos);
    }
}
