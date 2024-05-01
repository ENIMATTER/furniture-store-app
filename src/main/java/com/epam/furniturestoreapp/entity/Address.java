package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AddressID")
    private long addressID;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "ordertableid", referencedColumnName = "ordertableid")
    private OrderTable orderTableID;

    @Size(max = 50, message = "Street must be less than 50")
    @NotBlank(message = "Street is mandatory")
    @Column(name = "Street")
    private String street;

    @NotNull(message = "House is mandatory")
    @Column(name = "House")
    private Integer house;

    @NotNull(message = "Floor is mandatory")
    @Column(name = "Floor")
    private Integer floor;

    @NotNull(message = "Apartment is mandatory")
    @Column(name = "Apartment")
    private Integer apartment;

    @Size(max = 300, message = "message must be less than 300")
    @Column(name = "Message")
    private String message;
}
