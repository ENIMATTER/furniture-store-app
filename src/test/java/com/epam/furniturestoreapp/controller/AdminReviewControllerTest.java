package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.model.ReviewDto;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.*;

import static com.epam.furniturestoreapp.StaticVariablesForTests.*;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminReviewControllerTest {
    @InjectMocks
    private AdminReviewController adminReviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserTableService userTableService;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Test
    public void testGetReviewsAdmin() {
        List<Review> mockReviews = Arrays.asList(new Review(), new Review());

        when(reviewService.getAllReviews()).thenReturn(mockReviews);

        String result = adminReviewController.getReviewsAdmin(model);

        verify(reviewService, times(1)).getAllReviews();
        verifyModel(mockReviews);

        assertEquals("reviews-admin", result);
    }

    @Test
    public void testEditReviewAdmin_Success() {
        ReviewDto reviewDto = getTestReviewDto();
        UserTable user = getTestUser();
        Product product = getTestProduct();
        Review review = getTestReview();

        List<Review> productReviews = new ArrayList<>();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.existsById(any())).thenReturn(true);
        when(productService.existsById(any())).thenReturn(true);
        when(userTableService.getUserById(anyLong())).thenReturn(user);
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(reviewService.getById(any())).thenReturn(review);
        when(reviewService.getAllReviewsByProduct(any(Product.class))).thenReturn(productReviews);

        String result = adminReviewController.editReviewAdmin(reviewDto, bindingResult, model);

        verify(reviewService, times(1)).save(review);
        verify(productService, times(1)).save(product);
        verifyModel(productReviews);

        assertEquals("reviews-admin", result);
    }

    @Test
    public void testEditReviewAdmin_UserNotFound() {
        List<Review> productReviews = new ArrayList<>();
        ReviewDto reviewDto = getTestReviewDto();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.existsById(anyLong())).thenReturn(false);

        String result = adminReviewController.editReviewAdmin(reviewDto, bindingResult, model);

        verify(reviewService, never()).save(any(Review.class));
        verify(productService, never()).save(any(Product.class));
        verifyModel(productReviews);

        assertEquals("reviews-admin", result);
    }

    @Test
    public void testEditReviewAdmin_ProductNotFound() {
        List<Review> productReviews = new ArrayList<>();
        ReviewDto reviewDto = getTestReviewDto();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userTableService.existsById(anyLong())).thenReturn(true);
        when(productService.existsById(anyLong())).thenReturn(false);

        String result = adminReviewController.editReviewAdmin(reviewDto, bindingResult, model);

        verify(reviewService, never()).save(any(Review.class));
        verify(productService, never()).save(any(Product.class));
        verifyModel(productReviews);

        assertEquals("reviews-admin", result);
    }

    @Test
    public void testEditReviewAdmin_ValidationFailure() {
        List<Review> productReviews = new ArrayList<>();
        ReviewDto reviewDto = getTestReviewDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = adminReviewController.editReviewAdmin(reviewDto, bindingResult, model);

        verify(reviewService, never()).save(any(Review.class));
        verify(productService, never()).save(any(Product.class));
        verifyModel(productReviews);

        assertEquals("reviews-admin", result);
    }

    @Test
    public void testDeleteReviewAdmin() {
        List<Review> productReviews = new ArrayList<>();
        Long reviewId = 1L;

        when(reviewService.existById(reviewId)).thenReturn(true);

        String viewName = adminReviewController.deleteReviewAdmin(reviewId, model);

        verify(reviewService, times(1)).deleteById(reviewId);
        verifyModel(productReviews);

        assertEquals("reviews-admin", viewName);
    }

    @Test
    public void testDeleteReviewAdmin_ReviewNotFound() {
        List<Review> productReviews = new ArrayList<>();
        Long reviewId = 1L;

        when(reviewService.existById(reviewId)).thenReturn(false);

        String viewName = adminReviewController.deleteReviewAdmin(reviewId, model);

        verify(reviewService, never()).deleteById(reviewId);
        verifyModel(productReviews);

        assertEquals("reviews-admin", viewName);
    }

    private void verifyModel(List<Review> mockReviews) {
        verify(model).addAttribute("reviews", mockReviews);
        verify(model).addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
