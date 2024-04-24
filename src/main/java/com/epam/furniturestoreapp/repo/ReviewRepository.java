package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> getAllByProductID(Product productID);
}
