package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserTableService userTableService;
    private final CategoryService categoryService;

    public ReviewController(ProductService productService, ReviewService reviewService,
                            UserTableService userTableService, CategoryService categoryService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.userTableService = userTableService;
        this.categoryService = categoryService;
    }

    @PostMapping
    String postReview(@RequestParam("productID") long productID,
                      @RequestParam("rating") int rating,
                      @RequestParam("reviewComment") String reviewComment, Model model) {

        Product product = productService.getProductById(productID);
        LocalDateTime reviewDate = LocalDateTime.now();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(email);

        Review review = new Review();
        review.setProductID(product);
        review.setUserTableID(user);
        review.setRating(rating);
        review.setReviewComment(reviewComment);
        review.setReviewDate(reviewDate);
        reviewService.addReview(review);

        List<Review> reviews = reviewService.getAllReviewsByProduct(product);

        double sumRating = 0;
        for (Review r : reviews) {
            sumRating += r.getRating();
        }

        int countReviews = reviews.size();

        double averageRating = sumRating / countReviews;
        product.setAverageRating(averageRating);

        productService.save(product);

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        Category category = product.getCategoryID();
        reviews = reviewService.getAllReviewsByProduct(product);
        String image = Base64.getEncoder().encodeToString(product.getImage());
        model.addAttribute("reviews", reviews);
        model.addAttribute("category", category);
        model.addAttribute("product", product);
        model.addAttribute("image", image);

        return "shop-detail";
    }
}
