package com.epam.furniturestoreapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ProductUtil {
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
