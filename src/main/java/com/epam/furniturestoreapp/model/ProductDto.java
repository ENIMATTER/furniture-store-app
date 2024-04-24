package com.epam.furniturestoreapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private String productName;
    private String productDescription;
    private String categoryName;
    private Double price;
    private Integer stockQuantity;
    private String dimensions;
    private Material[] materials;
    private Color color;
    private MultipartFile image;
    private String imageString;

    public ProductDto(String productName, String productDescription, Double price, Integer stockQuantity,
                      String dimensions, Material[] materials, Color color) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.dimensions = dimensions;
        this.materials = materials;
        this.color = color;
    }
}
