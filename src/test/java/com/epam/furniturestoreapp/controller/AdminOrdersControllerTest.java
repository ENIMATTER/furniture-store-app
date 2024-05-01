package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.OrderTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

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
        List<Category> categories = new ArrayList<>();
        List<OrderTable> orders = new ArrayList<>();
        when(categoryService.findAll()).thenReturn(categories);
        when(orderTableService.getAll()).thenReturn(orders);

        String viewName = adminOrdersController.getOrdersAdmin(model);

        verifyModel(categories, orders);

        assertEquals("orders-admin", viewName);
    }

    @Test
    void testDeleteOrderAdmin_ExistingId() {
        List<Category> categories = new ArrayList<>();
        List<OrderTable> orders = new ArrayList<>();
        when(categoryService.findAll()).thenReturn(categories);
        when(orderTableService.getAll()).thenReturn(orders);
        Long orderId = 1L;
        when(orderTableService.existById(orderId)).thenReturn(true);

        String viewName = adminOrdersController.deleteOrderAdmin(orderId, model);

        verify(orderTableService, times(1)).deleteById(orderId);
        verifyModel(categories, orders);

        assertEquals("orders-admin", viewName);
    }

    @Test
    void testDeleteOrderAdmin_NonExistingId() {
        List<Category> categories = new ArrayList<>();
        List<OrderTable> orders = new ArrayList<>();
        Long orderId = 1L;
        when(orderTableService.existById(orderId)).thenReturn(false);

        String viewName = adminOrdersController.deleteOrderAdmin(orderId, model);

        verify(orderTableService, never()).deleteById(orderId);
        verifyModel(categories, orders);

        assertEquals("orders-admin", viewName);
    }

    private void verifyModel(List<Category> categories, List<OrderTable> orders) {
        verify(model, times(1)).addAttribute("categories", categories);
        verify(model, times(1)).addAttribute("orders", orders);
        verify(model, times(1)).addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
