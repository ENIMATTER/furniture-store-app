package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "category")
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CategoryID")
    private long categoryID;

    @Size(max = 50, message = "Category name must be less than 50")
    @NotBlank(message = "Category name is mandatory")
    @Column(name = "categoryname")
    private String categoryName;

}
