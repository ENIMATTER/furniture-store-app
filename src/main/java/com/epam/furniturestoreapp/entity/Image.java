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
@Table(name = "image")
public class Image {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ImageID")
    private Long imageID;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @Size(max = 200, message = "Image path must be less than 200")
    @NotBlank(message = "Image path is mandatory")
    @Column(name = "imagepath")
    private String imagePath;
}
