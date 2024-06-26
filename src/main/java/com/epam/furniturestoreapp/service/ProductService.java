package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.repo.ProductRepository;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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

    public Page<Product> getAllProductsByCategoryPage(Category category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productRepository.getAllByCategoryID(category, pageRequest);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProductsPage(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productRepository.findAll(pageRequest);
    }

    private List<Product> getAllProductsByMaterial(Material[] material) {
        List<Product> productList = productRepository.findAll();
        List<Product> result = new ArrayList<>();
        List<String> materialStrings = new ArrayList<>();
        for(Material m : material){
            materialStrings.add(m.name());
        }
        for(Product p : productList){
            String[] productMaterials = p.getMaterial().split(",");
            if(new HashSet<>(List.of(productMaterials)).containsAll(materialStrings)){
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> getAllByPriceBetweenAndColorAndMaterial(Double price, Double price2, Color color, Material[] material){
        List<Product> productList = new ArrayList<>();
        List<Product> allProductsByMaterial = new ArrayList<>();
        String colorStr = "";
        if(color != null){
            colorStr = color.name();
        }
        if(material != null){
            allProductsByMaterial = getAllProductsByMaterial(material);
        }
        if((price == null || price2 == null) && color != null && material != null){
            productList = productRepository.getAllByColor(colorStr);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color != null && material != null){
            productList = productRepository.getAllByPriceBetweenAndColor(price, price2, colorStr);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color != null && material == null){
            productList = productRepository.getAllByPriceBetweenAndColor(price, price2, colorStr);
        }
        if(price != null && price2 != null && color == null && material != null){
            productList = productRepository.getAllByPriceBetween(price, price2);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color == null && material == null){
            productList = productRepository.getAllByPriceBetween(price, price2);
        }
        if(price == null && price2 == null && color != null && material == null){
            productList = productRepository.getAllByColor(colorStr);
        }
        if(price == null && price2 == null && color == null && material != null){
            productList = allProductsByMaterial;
        }
        return productList;
    }

    public List<Product> getAllByCategoryIDAndProductNameContaining(Category categoryID, String productName){
        return productRepository.getAllByCategoryIDAndProductNameContaining(categoryID, productName);
    }

    public List<Product> getAllByCategoryIDAndPriceBetweenAndColorAndMaterial(Category category, Double price, Double price2, Color color, Material[] material) {
        List<Product> productList = new ArrayList<>();
        List<Product> allProductsByMaterial = new ArrayList<>();
        String colorStr = "";
        if(color != null){
            colorStr = color.name();
        }
        if(material != null){
            allProductsByMaterial = getAllProductsByMaterial(material);
        }
        if((price == null || price2 == null) && color != null && material != null){
            productList = productRepository.getAllByCategoryIDAndColor(category, colorStr);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color != null && material != null){
            productList = productRepository.getAllByCategoryIDAndPriceBetweenAndColor(category, price, price2, colorStr);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color != null && material == null){
            productList = productRepository.getAllByCategoryIDAndPriceBetweenAndColor(category, price, price2, colorStr);
        }
        if(price != null && price2 != null && color == null && material != null){
            productList = productRepository.getAllByCategoryIDAndPriceBetween(category, price, price2);
            productList.retainAll(allProductsByMaterial);
        }
        if(price != null && price2 != null && color == null && material == null){
            productList = productRepository.getAllByCategoryIDAndPriceBetween(category, price, price2);
        }
        if(price == null && price2 == null && color != null && material == null){
            productList = productRepository.getAllByCategoryIDAndColor(category, colorStr);
        }
        if(price == null && price2 == null && color == null && material != null){
            List<Product> productsByCategory = productRepository.getAllByCategoryID(category);
            productsByCategory.retainAll(allProductsByMaterial);
            productList = productsByCategory;
        }
        return productList;
    }

    public List<Product> getAllByProductNameContaining(String productName){
        return productRepository.getAllByProductNameContaining(productName);
    }


    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public boolean existsById(Long productID) {
        return productRepository.existsById(productID);
    }


}
