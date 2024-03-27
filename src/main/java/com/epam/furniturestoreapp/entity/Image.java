package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "image")
public class Image {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ImageID")
    private long imageID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @Size(max = 200, message = "Image path must be less than 200")
    @NotBlank(message = "Image path is mandatory")
    @Column(name = "ImagePath")
    private String imagePath;
}
