package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByCategoryID(Category category);
    List<Product> getAllByPriceBetweenAndColor(double price, double price2, String color);
    List<Product> getAllByPriceBetween(double price, double price2);
    List<Product> getAllByColor(String color);
    List<Product> getAllByCategoryIDAndPriceBetweenAndColor(Category categoryID, double price, double price2, String color);
    List<Product> getAllByCategoryIDAndPriceBetween(Category categoryID, double price, double price2);
    List<Product> getAllByCategoryIDAndColor(Category categoryID, String color);
    List<Product> getAllByProductNameContaining(String productName);
    List<Product> getAllByCategoryIDAndProductNameContaining(Category categoryID, String productName);
    Page<Product> getAllByCategoryID(Category categoryID, Pageable pageable);
    Page<Product> getAllByCategoryIDAndProductNameContaining(Category categoryID, String productName, Pageable pageable);
}
