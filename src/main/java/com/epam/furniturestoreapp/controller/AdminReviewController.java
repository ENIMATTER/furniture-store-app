package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import com.epam.furniturestoreapp.model.ReviewDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
@RequestMapping("/reviews-admin")
public class AdminReviewController {
    private final ReviewService reviewService;
    private final UserTableService userTableService;
    private final ProductService productService;

    @Autowired
    public AdminReviewController(ReviewService reviewService, UserTableService userTableService,
                                 ProductService productService) {
        this.reviewService = reviewService;
        this.userTableService = userTableService;
        this.productService = productService;
    }

    @GetMapping
    public String getReviewsAdmin(Model model){
        addToModelBasicAttributes(model);
        return "reviews-admin";
    }

    @PutMapping
    public String editReviewAdmin(@Valid @ModelAttribute("reviewUtil") ReviewDto reviewUtil,
                                  BindingResult result, Model model){
        if (result.hasErrors()) {
            addToModelBasicAttributes(model);
            model.addAttribute("fail", true);
            return "reviews-admin";
        }
        if(!userTableService.existsById(reviewUtil.getUserTableID())){
            addToModelBasicAttributes(model);
            model.addAttribute("usererror", true);
            return "reviews-admin";
        }
        if(!productService.existsById(reviewUtil.getProductID())){
            addToModelBasicAttributes(model);
            model.addAttribute("producterror", true);
            return "reviews-admin";
        }
        UserTable user = userTableService.getUserById(reviewUtil.getUserTableID());
        Product product = productService.getProductById(reviewUtil.getProductID());

        Review editedReview = reviewService.getById(reviewUtil.getReviewID());
        editedReview.setProductID(product);
        editedReview.setUserTableID(user);
        editedReview.setRating(reviewUtil.getRating());
        editedReview.setReviewComment(reviewUtil.getReviewComment());
        editedReview.setReviewDate(reviewUtil.getReviewDate());
        reviewService.save(editedReview);

        Double newAverageRating = 0.0;
        List<Review> productReviews = reviewService.getAllReviewsByProduct(product);
        for(Review review : productReviews){
            newAverageRating += review.getRating();
        }
        newAverageRating = newAverageRating / productReviews.size();
        product.setAverageRating(newAverageRating);
        productService.save(product);

        addToModelBasicAttributes(model);

        return "reviews-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteReviewAdmin(@PathVariable Long id, Model model){
        if(reviewService.existById(id)){
            reviewService.deleteById(id);
        }
        addToModelBasicAttributes(model);
        return "reviews-admin";
    }

    private void addToModelBasicAttributes(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
