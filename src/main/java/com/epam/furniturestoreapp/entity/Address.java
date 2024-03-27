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
@Table(name = "address")
public class Address {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AddressID")
    private long addressID;

    @ManyToOne
    @JoinColumn(name = "userTableID", referencedColumnName = "userTableID")
    private UserTable userTableID;

    @Size(max = 50, message = "Street must be less than 50")
    @NotBlank(message = "Street is mandatory")
    @Column(name = "Street")
    private String street;

    @Size(max = 50, message = "City must be less than 50")
    @NotBlank(message = "City is mandatory")
    @Column(name = "City")
    private String city;

    @Size(max = 50, message = "State must be less than 50")
    @NotBlank(message = "State is mandatory")
    @Column(name = "State")
    private String state;

    @Size(max = 50, message = "Country must be less than 50")
    @NotBlank(message = "Country is mandatory")
    @Column(name = "Country")
    private String country;

    @Size(max = 50, message = "Zip code must be less than 50")
    @NotBlank(message = "ZipCode is mandatory")
    @Column(name = "ZipCode")
    private String zipCode;
}
