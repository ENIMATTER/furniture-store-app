package com.epam.furniturestoreapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ProductDto {
    @Size(max = 50, message = "Product name must be less than 50")
    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @Size(max = 2000, message = "Product description must be less than 2000")
    @NotBlank(message = "Product description is mandatory")
    private String productDescription;

    @Size(max = 50, message = "Category name must be less than 50")
    @NotBlank(message = "Category name is mandatory")
    private String categoryName;

    @NotNull(message = "Price is mandatory")
    private Double price;

    @NotNull(message = "Stock quantity is mandatory")
    private Integer stockQuantity;

    @Size(max = 100, message = "Dimensions must be less than 100")
    @NotBlank(message = "Dimensions is mandatory")
    private String dimensions;

    @NotNull(message = "Materials is mandatory")
    private Material[] materials;

    @NotNull(message = "Color is mandatory")
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
