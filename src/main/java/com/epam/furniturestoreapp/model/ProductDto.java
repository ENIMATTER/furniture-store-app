package com.epam.furniturestoreapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ProductDto {
    private String productName;
    private String productDescription;
    private String categoryName;
    private Double price;
    private Integer stockQuantity;
    private String dimensions;
    private Material[] materials;
    private Color color;
    private String imagePath;
}
