package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static com.epam.furniturestoreapp.StaticVariablesForTests.*;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserTableService userTableService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    public void testPostReview() {
        Product product = getTestProduct();
        UserTable user = getTestUser();
        List<Review> reviews = new ArrayList<>();

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(userTableService.getUserByEmail(any())).thenReturn(user);
        when(reviewService.getAllReviewsByProduct(product)).thenReturn(reviews);

        String result = reviewController.postReview(product.getProductID(), 5,
                "Great product!", model);

        verify(productService, times(1)).getProductById(anyLong());
        verify(userTableService, times(1)).getUserByEmail(any());
        verify(reviewService, times(1)).addReview(any(Review.class));
        verify(reviewService, times(2)).getAllReviewsByProduct(any(Product.class));
        verify(productService, times(1)).save(any(Product.class));
        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("categories"), anyList());
        verify(model, times(1)).addAttribute(eq("thAction"), eq(TH_ACTION_FOR_ALL_PRODUCTS));
        verify(model, times(1)).addAttribute(eq("reviews"), anyList());
        verify(model, times(1)).addAttribute(eq("category"), any(Category.class));
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
        verify(model, times(1)).addAttribute(eq("image"), anyString());

        assertEquals("shop-detail", result);
    }
}
