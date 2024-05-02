package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestCategory;
import static com.epam.furniturestoreapp.StaticVariablesForTests.getTestProduct;
import static com.epam.furniturestoreapp.model.StaticVariables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductsControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductsController productsController;

    @Test
    public void testGetProductsByCategoryId_CategoryExists() {
        Category category = getTestCategory();
        List<Product> products = new ArrayList<>();
        Page<Product> productPage = new PageImpl<>(products);

        when(categoryService.findByName(anyString())).thenReturn(category);
        when(productService.getAllProductsByCategoryPage(category, 0, 9)).thenReturn(productPage);

        String result = productsController.getProductsByCategoryId(category.getCategoryName(), 1, 9, model);

        verify(categoryService, times(1)).findByName(category.getCategoryName());
        verify(productService, times(1)).getAllProductsByCategoryPage(category, 0, 9);
        verify(model, times(1)).addAttribute(eq("thAction"),
                eq(TH_ACTION_FOR_PRODUCTS_BY_CATEGORY + category.getCategoryName()));
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testGetProductsByCategoryId_CategoryDoesNotExist() {
        when(categoryService.findAll()).thenReturn(new ArrayList<>());
        when(categoryService.findByName(anyString())).thenReturn(null);

        String result = productsController.getProductsByCategoryId(anyString(), 1, 9, model);

        verify(categoryService, times(1)).findByName(anyString());
        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));

        assertEquals("not-found", result);
    }

    @Test
    public void testPostProductsByCategoryId_NoSearch_NoFilter_NoMaterials_NoColor() {
        Category category = getTestCategory();

        List<Product> products = new ArrayList<>();

        when(categoryService.findByName(anyString())).thenReturn(category);
        when(productService.getAllProductsByCategory(category)).thenReturn(products);

        String result = productsController.postProductsByCategoryId(anyString(), "", "",
                null, null, null, null, 1, 9, model);

        verify(categoryService, times(1)).findByName(anyString());
        verify(productService, times(1)).getAllProductsByCategory(category);
        verify(productService, never()).getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(any(),
                anyDouble(), anyDouble(), any(), any());
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testPostProductsByCategoryId_SearchQuery() {
        Category category = getTestCategory();
        List<Product> products = new ArrayList<>();

        when(categoryService.findByName(anyString())).thenReturn(category);
        when(productService.getAllByCategoryIDAndProductNameContaining(category, "search"))
                .thenReturn(products);

        String result = productsController.postProductsByCategoryId(category.getCategoryName(),
                "search", "", null, null, null, null, 1, 9, model);


        verify(categoryService, times(1)).findByName(category.getCategoryName());
        verify(productService, times(1))
                .getAllByCategoryIDAndProductNameContaining(category, "search");
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testPostProductsByCategoryId_FilterByPriceColorMaterial() {
        Category category = getTestCategory();
        List<Product> products = new ArrayList<>();

        when(categoryService.findByName(anyString())).thenReturn(category);
        when(productService.getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(category, 10.0,
                20.0, Color.RED, new Material[]{Material.WOOD})).thenReturn(products);

        String result = productsController.postProductsByCategoryId(category.getCategoryName(),
                "", "", 10.0, 20.0, Color.RED, new Material[]{Material.WOOD},
                1, 9, model);

        verify(categoryService, times(1)).findByName(category.getCategoryName());
        verify(productService, times(1))
                .getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(category, 10.0, 20.0,
                        Color.RED, new Material[]{Material.WOOD});
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testGetProductsPage() {
        List<Product> products = new ArrayList<>();
        Page<Product> productPage = new PageImpl<>(products);

        when(productService.getAllProductsPage(0, 9)).thenReturn(productPage);

        String result = productsController.getProductsPage(1, 9, model);

        verify(productService, times(1)).getAllProductsPage(0, 9);
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testPostProducts_NoSearch_NoFilter_NoMaterials_NoColor() {
        List<Product> products = new ArrayList<>();

        when(productService.getAllProducts()).thenReturn(products);

        String result = productsController.postProducts("", "", null, null, null,
                null, 1, 9, model);

        verify(productService, times(1)).getAllProducts();
        verify(productService, never()).getAllByProductNameContaining(anyString());
        verify(productService, never()).getAllByPriceBetweenAndColorAndMaterial(anyDouble(), anyDouble(), any(), any());
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testPostProducts_SearchQuery() {
        List<Product> products = new ArrayList<>();

        when(productService.getAllByProductNameContaining("search")).thenReturn(products);

        String result = productsController.postProducts("search", "", null,
                null, null, null, 1, 9, model);

        verify(productService, times(1)).getAllByProductNameContaining("search");
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testPostProducts_FilterByPriceColorMaterial() {
        List<Product> products = new ArrayList<>();

        when(productService.getAllByPriceBetweenAndColorAndMaterial(10.0, 20.0, Color.RED,
                new Material[]{Material.WOOD})).thenReturn(products);

        String result = productsController.postProducts("", "", 10.0, 20.0, Color.RED,
                new Material[]{Material.WOOD}, 1, 9, model);

        verify(productService, times(1)).getAllByPriceBetweenAndColorAndMaterial(10.0, 20.0, Color.RED, new Material[]{Material.WOOD});
        verifyModel();

        assertEquals("shop", result);
    }

    @Test
    public void testGetProductById_ProductExists() {
        Product product = getTestProduct();
        Category category = getTestCategory();
        List<Review> reviews = new ArrayList<>();

        when(productService.getProductById(anyLong())).thenReturn(product);
        when(categoryService.findAll()).thenReturn(new ArrayList<>());
        when(reviewService.getAllReviewsByProduct(product)).thenReturn(reviews);

        String result = productsController.getProductById(1L, model);

        verify(productService, times(1)).getProductById(anyLong());
        verify(categoryService, times(1)).findAll();
        verify(reviewService, times(1)).getAllReviewsByProduct(product);
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
        verify(model, times(1)).addAttribute(eq("product"), eq(product));
        verify(model, times(1)).addAttribute(eq("category"), eq(category));
        verify(model, times(1)).addAttribute(eq("reviews"), eq(reviews));
        verify(model, times(1)).addAttribute(eq("image"), anyString());

        assertEquals("shop-detail", result);
    }

    @Test
    public void testGetProductById_ProductDoesNotExist() {
        List<Category> categories = new ArrayList<>();

        when(productService.getProductById(anyLong())).thenReturn(null);
        when(categoryService.findAll()).thenReturn(categories);

        String result = productsController.getProductById(1L, model);

        verify(productService, times(1)).getProductById(1L);
        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), anyString());

        assertEquals("not-found", result);
    }

    private void verifyModel() {
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("filterList"), anyList());
        verify(model, times(1)).addAttribute(eq("colorMap"), anyMap());
        verify(model, times(1)).addAttribute(eq("materialList"), anyList());

        verify(model, times(1)).addAttribute(eq("shopItems"), anyList());
        verify(model, times(1)).addAttribute(eq("countOfAllProducts"), anyLong());
        verify(model, times(1)).addAttribute(eq("pages"), anyMap());
        verify(model, times(1)).addAttribute(eq("currentPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("maxPage"), anyInt());
        verify(model, times(1)).addAttribute(eq("category"), any());
    }
}

