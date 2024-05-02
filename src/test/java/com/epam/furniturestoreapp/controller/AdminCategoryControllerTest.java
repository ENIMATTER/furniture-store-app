package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestCategory;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminCategoryControllerTest {

    @Mock
    private Model model;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AdminCategoryController adminCategoryController;

    @Test
    void testGetCategoriesAdmin() {
        List<Category> categories = new ArrayList<>();

        when(categoryService.findAll()).thenReturn(categories);

        String result = adminCategoryController.getCategoriesAdmin(model);

        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    public void testEditCategoryAdmin_Success() {
        Category categoryEdit = getTestCategory();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.existByName(categoryEdit.getCategoryName())).thenReturn(false);
        when(categoryService.findById(categoryEdit.getCategoryID())).thenReturn(categoryEdit);

        String result = adminCategoryController.editCategoryAdmin(categoryEdit, bindingResult, model);

        verify(categoryService, times(1)).findById(categoryEdit.getCategoryID());
        verify(categoryService, times(1)).save(categoryEdit);
        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    void testEditCategoryAdmin_Fail() {
        Category category = getTestCategory();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = adminCategoryController.editCategoryAdmin(category, bindingResult, model);

        verify(categoryService, never()).save(any(Category.class));
        verify(model, times(1)).addAttribute(eq("fail"), eq(true));
        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    void testEditCategoryAdmin_CategoryExists() {
        Category category = getTestCategory();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.existByName(category.getCategoryName())).thenReturn(true);

        String result = adminCategoryController.editCategoryAdmin(category, bindingResult, model);

        verify(categoryService, never()).findById(category.getCategoryID());
        verify(categoryService, never()).save(category);
        verify(model, times(1)).addAttribute(eq("exist"), eq(true));
        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    void testAddCategoryAdmin_Success() {
        String categoryName = "Test Category";

        when(categoryService.existByName(categoryName)).thenReturn(false);

        String result = adminCategoryController.addCategoryAdmin(categoryName, model);

        verify(categoryService, times(1)).save(any(Category.class));
        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    void testAddCategoryAdmin_CategoryExists() {
        String categoryName = "Test Category";

        when(categoryService.existByName(categoryName)).thenReturn(true);

        String result = adminCategoryController.addCategoryAdmin(categoryName, model);

        verify(categoryService, never()).save(any(Category.class));
        verifyModel();

        assertEquals("categories-admin", result);
    }

    @Test
    public void testDeleteCategoryAdmin() {
        Long categoryIdToDelete = 1L;

        when(categoryService.existById(categoryIdToDelete)).thenReturn(true);

        String result = adminCategoryController.deleteCategoryAdmin(categoryIdToDelete, model);

        verify(categoryService, times(1)).existById(categoryIdToDelete);
        verify(categoryService, times(1)).deleteByID(categoryIdToDelete);
        verifyModel();

        assertEquals("categories-admin", result);
    }

    private void verifyModel() {
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
    }
}
