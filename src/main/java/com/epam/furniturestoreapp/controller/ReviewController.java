package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserTableService userTableService;

    private final List<Category> categories;

    public ReviewController(CategoryService categoryService, ProductService productService, ReviewService reviewService, UserTableService userTableService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
        this.userTableService = userTableService;
        categories = categoryService.findAll();
    }

    @PostMapping
    String postReview(@RequestParam("productID") long productID,
                      @RequestParam("userTableID") long userTableID,
                      @RequestParam("rating") int rating,
                      @RequestParam("reviewComment") String reviewComment,
                      Model model) {

        Product product = productService.getProductById(productID);
        UserTable user = userTableService.getUserById(userTableID);
        LocalDateTime reviewDate = LocalDateTime.now();

        Review review = new Review();
        review.setProductID(product);
        review.setUserTableID(user);
        review.setRating(rating);
        review.setReviewComment(reviewComment);
        review.setReviewDate(reviewDate);
        reviewService.addReview(review);

        Category category = product.getCategoryID();
        List<Review> reviews = reviewService.getAllReviewsByProduct(product);

        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        return "shop-detail";
    }
}
