package com.epam.furniturestoreapp.model;

import com.epam.furniturestoreapp.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class AdminProductsDto {
    private Long productID;
    private String imageString;
    private String productName;
    private String color;
    private String category;
    private double price;
    private int stockQuantity;

    public AdminProductsDto(Product product) {
        this.productID = product.getProductID();
        this.imageString = Base64.getEncoder().encodeToString(product.getImage());
        this.productName = product.getProductName();
        this.color = product.getColor();
        this.category = product.getCategoryID().getCategoryName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
}
