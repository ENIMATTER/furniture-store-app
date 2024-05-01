package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CategoryID")
    private Long categoryID;

    @Size(max = 50, message = "Category name must be less than 50")
    @NotBlank(message = "Category name is mandatory")
    @Column(name = "categoryname")
    private String categoryName;

    @OneToMany
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID")
    List<Product> products;
}
