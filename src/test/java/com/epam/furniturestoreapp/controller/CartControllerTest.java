package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.furniturestoreapp.StaticVariablesForTests.*;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    CartItemService cartItemService;

    @Mock
    UserTableService userTableService;

    @Mock
    ProductService productService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CartController cartController;

    @Mock
    Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    public void testGetCart() {
        SecurityContextHolder.setContext(securityContext);
        List<CartItem> cartItems = new ArrayList<>();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(cartItemService.getAllItemsByUser(any())).thenReturn(cartItems);

        String result = cartController.getCart(model);

        verify(cartItemService, times(1)).getAllItemsByUser(any());
        verifyModel();

        assertEquals("cart", result);
    }

    @Test
    public void testAddToCartItem_Success() {
        SecurityContextHolder.setContext(securityContext);
        long cartProductID = 1L;
        Integer cartQuantity = 2;
        Product product = getTestProduct();
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(productService.getProductById(cartProductID)).thenReturn(product);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(cartItemService.getAllItemsByUser(any())).thenReturn(new ArrayList<>());
        when(cartItemService.getAll()).thenReturn(new ArrayList<>());

        String result = cartController.addToCartItem(cartProductID, cartQuantity, model);

        verify(cartItemService, times(1)).saveAll(anyList());
        verifyModel();

        assertEquals("cart", result);
    }

    @Test
    public void testDeleteCartItem_Success() {
        SecurityContextHolder.setContext(securityContext);
        Long cartItemID = 1L;

        when(securityContext.getAuthentication()).thenReturn(authentication);

        String result = cartController.deleteCartItem(cartItemID, model);

        verify(cartItemService, times(1)).deleteById(cartItemID);
        verifyModel();

        assertEquals("cart", result);
    }

    @Test
    public void testChangeQuantityCartItem_Success() {
        Long cartItemID = 1L;
        Integer cartQuantity = 3;
        CartItem cartItem = getTestCartItem();
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(cartItemService.getById(cartItemID)).thenReturn(cartItem);

        String result = cartController.changeQuantityCartItem(cartItemID, cartQuantity, model);

        verify(cartItemService, times(1)).save(any(CartItem.class));
        verifyModel();

        assertEquals("cart", result);
    }

    private void verifyModel(){
        verify(model, times(1)).addAttribute(eq("allTotal"), any(BigDecimal.class));
        verify(model, times(1)).addAttribute(eq("cartItemDtos"), anyList());
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}