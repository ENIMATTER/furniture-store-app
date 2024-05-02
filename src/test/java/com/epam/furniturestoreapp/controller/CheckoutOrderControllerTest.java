package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.AddressDto;
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
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestAddressDto;
import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestUser;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutOrderControllerTest {

    @Mock
    AddressService addressService;

    @Mock
    UserTableService userTableService;

    @Mock
    CartItemService cartItemService;

    @Mock
    OrderTableService orderTableService;

    @Mock
    OrderItemService orderItemService;

    @Mock
    ProductService productService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CheckoutOrderController checkoutOrderController;

    @Mock
    Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    BindingResult bindingResult;

    @Test
    public void testCheckout_Success() {
        SecurityContextHolder.setContext(securityContext);
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(cartItemService.getAllItemsByUser(any(UserTable.class))).thenReturn(new ArrayList<>());

        String result = checkoutOrderController.checkout(model);

        verify(cartItemService, times(1)).getAllItemsByUser(any(UserTable.class));
        verifyModelCheckout();

        assertEquals("checkout", result);
    }

    @Test
    public void testCheckoutAndFormOrder_Success() {
        SecurityContextHolder.setContext(securityContext);
        AddressDto addressDto = getTestAddressDto();
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(cartItemService.getAllItemsByUser(any(UserTable.class))).thenReturn(new ArrayList<>());

        String result = checkoutOrderController.checkoutAndFormOrder(addressDto, 0.0, bindingResult, model);

        verify(orderTableService, times(1)).addOrder(any(OrderTable.class));
        verify(addressService, times(1)).addAddress(any(Address.class));
        verify(orderItemService, times(1)).addAllOrderItems(anyList());
        verify(cartItemService, times(1)).deleteAll(anyList());
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));

        assertEquals("account", result);
    }

    @Test
    public void testCheckoutAndFormOrder_ValidationFailure() {
        SecurityContextHolder.setContext(securityContext);
        AddressDto addressDto = getTestAddressDto();
        UserTable user = getTestUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(cartItemService.getAllItemsByUser(any())).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = checkoutOrderController.checkoutAndFormOrder(addressDto, 0.0, bindingResult, model);

        verify(orderTableService, never()).addOrder(any(OrderTable.class));
        verify(addressService, never()).addAddress(any(Address.class));
        verify(orderItemService, never()).addAllOrderItems(anyList());
        verify(cartItemService, never()).deleteAll(anyList());
        verify(model).addAttribute(eq("fail"), eq(true));
        verifyModelCheckout();

        assertEquals("checkout", result);
    }

    @Test
    public void testCheckoutAndFormOrder_LowBalance() {
        SecurityContextHolder.setContext(securityContext);
        AddressDto addressDto = getTestAddressDto();
        UserTable user = getTestUser();
        user.setBalance(50.0);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(cartItemService.getAllItemsByUser(any())).thenReturn(new ArrayList<>());
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = checkoutOrderController.checkoutAndFormOrder(addressDto, 100.0, bindingResult, model);

        verify(orderTableService, never()).addOrder(any(OrderTable.class));
        verify(addressService, never()).addAddress(any(Address.class));
        verify(orderItemService, never()).addAllOrderItems(anyList());
        verify(cartItemService, never()).deleteAll(anyList());
        verify(model).addAttribute(eq("lowbalance"), eq(true));
        verifyModelCheckout();

        assertEquals("checkout", result);
    }

    @Test
    public void testOrders_Success() {
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userTableService.getUserByEmail(any())).thenReturn(new UserTable());
        when(orderTableService.getAllByUser(any(UserTable.class))).thenReturn(new ArrayList<>());

        String result = checkoutOrderController.orders(model);

        verify(orderTableService, times(1)).getAllByUser(any(UserTable.class));
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
        verify(model, times(1)).addAttribute(eq("orderDtos"), anyList());

        assertEquals("orders", result);
    }

    private void verifyModelCheckout(){
        verify(model, times(1)).addAttribute(eq("total"), any(BigDecimal.class));
        verify(model, times(1)).addAttribute(eq("shippingFee"), any(BigDecimal.class));
        verify(model, times(1)).addAttribute(eq("sumCartItems"), any(BigDecimal.class));
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}