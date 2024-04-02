package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.repo.ProductRepository;
import com.epam.furniturestoreapp.util.Color;
import com.epam.furniturestoreapp.util.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Product> getAllProductsInRange(Double from, Double to) {
        List<Product> productList = productRepository.findAll();
        productList.removeIf(p -> p.getPrice() < from || p.getPrice() > to);
        return productList;
    }

    public List<Product> getAllProductsByColor(Color color) {
        List<Product> productList = productRepository.findAll();
        productList.removeIf(p -> !color.name().equals(p.getColor()));
        return productList;
    }

    public List<Product> getAllProductsByMaterial(Material[] material) {
        List<Product> productList = productRepository.findAll();
        List<Product> result = new ArrayList<>();
        List<String> materialStrings = new ArrayList<>();
        for(Material m : material){
            materialStrings.add(m.name());
        }
        for(Product p : productList){
            String[] productMaterials = p.getMaterial().split(",");
            if(List.of(productMaterials).containsAll(materialStrings)){
                result.add(p);
            }
        }
        return result;
    }
}
