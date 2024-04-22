package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.service.ReviewService;
import com.epam.furniturestoreapp.service.UserTableService;
import com.epam.furniturestoreapp.util.ReviewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

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
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("thAction", thActionForAllProducts);
        return "reviews-admin";
    }

    @PutMapping
    public String editReviewAdmin(@ModelAttribute("reviewUtil") ReviewUtil reviewUtil){
        if(!userTableService.existsById(reviewUtil.getUserTableID())){
            return "redirect:/reviews-admin?usererror";
        }
        if(!productService.existsById(reviewUtil.getProductID())){
            return "redirect:/reviews-admin?producterror";
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

        return "redirect:/reviews-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteReviewAdmin(@PathVariable Long id){
        if(reviewService.existById(id)){
            reviewService.deleteById(id);
        }
        return "redirect:/reviews-admin";
    }
}
