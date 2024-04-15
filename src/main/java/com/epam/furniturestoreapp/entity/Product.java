package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "product")
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ProductID")
    private Long productID;

    @Size(max = 50, message = "Product name must be less than 50")
    @NotBlank(message = "Product name is mandatory")
    @Column(name = "productname")
    private String productName;

    @Size(max = 2000, message = "Product description must be less than 2000")
    @NotBlank(message = "Product description is mandatory")
    @Column(name = "productdescription")
    private String productDescription;

    @ManyToOne
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
    private Category categoryID;

    @NotBlank(message = "Price is mandatory")
    @Column(name = "price")
    private Double price;

    @NotBlank(message = "Stock quantity is mandatory")
    @Column(name = "stockquantity")
    private Integer stockQuantity;

    @Size(max = 100, message = "Dimensions must be less than 100")
    @NotBlank(message = "Dimensions is mandatory")
    @Column(name = "Dimensions")
    private String dimensions;

    @Size(max = 100, message = "Material must be less than 100")
    @NotBlank(message = "Material is mandatory")
    @Column(name = "Material")
    private String material;

    @Size(max = 100, message = "Color must be less than 100")
    @NotBlank(message = "Color is mandatory")
    @Column(name = "Color")
    private String color;

    @NotBlank(message = "Average rating is mandatory")
    @Column(name = "averagerating")
    private Double averageRating;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Image image;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private List<OrderItem> orderItems;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private List<CartItem> cartItems;
}
