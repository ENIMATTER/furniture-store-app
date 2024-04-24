package com.epam.furniturestoreapp.model;

import com.epam.furniturestoreapp.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class ShopItemDto {
    private Long productID;
    private String imageString;
    private double averageRating;
    private String productName;
    private double price;

    public ShopItemDto(Product product) {
        this.productID = product.getProductID();
        this.imageString = Base64.getEncoder().encodeToString(product.getImage());
        this.averageRating = product.getAverageRating();
        this.productName = product.getProductName();
        this.price = product.getPrice();
    }
}
