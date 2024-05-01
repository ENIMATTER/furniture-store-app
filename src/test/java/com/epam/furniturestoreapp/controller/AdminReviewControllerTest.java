package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
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

import java.time.LocalDateTime;
import java.util.*;

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

    private UserTable getTestUser(){
        UserTable user = new UserTable("firstname", "lastname", "email@text.com", "userPassword",
                "123123123", 0.0, "roles");
        user.setUserTableID(1L);
        return user;
    }

    private Product getTestProduct(){
        Product product = new Product("productName", "productDescription",
                getTestCategory(), 100.0, 100, "dimensions", "material",
                "color", 5.0);
        product.setImage(new byte[1]);
        product.setProductID(1L);
        return product;
    }

    private Category getTestCategory(){
        Category testCategory = new Category();
        testCategory.setCategoryID(1L);
        testCategory.setCategoryName("testCategory");
        return testCategory;
    }

    private Review getTestReview(){
        Review review = new Review();
        review.setReviewID(1L);
        review.setProductID(getTestProduct());
        review.setUserTableID(getTestUser());
        review.setRating(5);
        review.setReviewComment("ReviewComment");
        review.setReviewDate(LocalDateTime.now());
        return review;
    }

    private ReviewDto getTestReviewDto(){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewID(1L);
        reviewDto.setProductID(1L);
        reviewDto.setUserTableID(1L);
        reviewDto.setRating(5);
        reviewDto.setReviewComment("ReviewComment");
        reviewDto.setReviewDate(LocalDateTime.now());
        return reviewDto;
    }
}
