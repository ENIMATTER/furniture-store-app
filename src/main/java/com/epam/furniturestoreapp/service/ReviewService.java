package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviewsByProduct(Product productID){
        return reviewRepository.getAllByProductID(productID);
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getAllReviewsByUser(UserTable user) {
        return reviewRepository.getAllByUserTableID(user);
    }

    public void deleteReviews(List<Review> reviews) {
        reviewRepository.deleteAll(reviews);
    }
}
