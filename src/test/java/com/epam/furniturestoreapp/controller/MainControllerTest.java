package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {
    @Mock
    private Model model;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private MainController mainController;

    @Test
    public void testMainPage() {
        when(categoryService.findAll()).thenReturn(new ArrayList<>());

        String result = mainController.mainPage(model);

        verifyModel();

        assertEquals("index", result);
    }

    @Test
    public void testNotFound() {
        when(categoryService.findAll()).thenReturn(new ArrayList<>());

        String result = mainController.notFound(model);

        verifyModel();

        assertEquals("not-found", result);
    }

    @Test
    public void testAdminPanel() {
        when(categoryService.findAll()).thenReturn(new ArrayList<>());

        String result = mainController.adminPanel(model);

        verifyModel();

        assertEquals("admin", result);
    }

    private void verifyModel(){
        verify(model, times(1)).addAttribute("categories", new ArrayList<>());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}
