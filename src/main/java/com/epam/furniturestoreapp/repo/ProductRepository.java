package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByCategoryID(Category category);
}
