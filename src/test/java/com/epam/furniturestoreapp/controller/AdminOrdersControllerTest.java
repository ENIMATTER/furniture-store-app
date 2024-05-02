package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.OrderTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminOrdersControllerTest {
    @Mock
    private OrderTableService orderTableService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminOrdersController adminOrdersController;

    @Test
    void testGetOrdersAdmin() {
        when(categoryService.findAll()).thenReturn(new ArrayList<>());
        when(orderTableService.getAll()).thenReturn(new ArrayList<>());

        String result = adminOrdersController.getOrdersAdmin(model);

        verifyModel();

        assertEquals("orders-admin", result);
    }

    @Test
    void testDeleteOrderAdmin_ExistingId() {
        Long orderId = 1L;

        when(categoryService.findAll()).thenReturn(new ArrayList<>());
        when(orderTableService.getAll()).thenReturn(new ArrayList<>());
        when(orderTableService.existById(orderId)).thenReturn(true);

        String result = adminOrdersController.deleteOrderAdmin(orderId, model);

        verify(orderTableService, times(1)).deleteById(orderId);
        verifyModel();

        assertEquals("orders-admin", result);
    }

    @Test
    void testDeleteOrderAdmin_NonExistingId() {
        Long orderId = 1L;

        when(orderTableService.existById(orderId)).thenReturn(false);

        String result = adminOrdersController.deleteOrderAdmin(orderId, model);

        verify(orderTableService, never()).deleteById(orderId);
        verifyModel();

        assertEquals("orders-admin", result);
    }

    private void verifyModel() {
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute("orders", new ArrayList<>());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}
