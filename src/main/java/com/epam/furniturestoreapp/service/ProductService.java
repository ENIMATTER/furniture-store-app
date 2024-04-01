package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProductsByCategory(Category category) {
        return productRepository.getAllByCategoryID(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsByName(String name) {
        return productRepository.getAllByProductName(name);
    }
}
