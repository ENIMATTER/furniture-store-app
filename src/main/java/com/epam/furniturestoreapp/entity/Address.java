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
@Table(name = "address")
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

    @NotBlank(message = "House is mandatory")
    @Column(name = "House")
    private Integer house;

    @NotBlank(message = "Floor is mandatory")
    @Column(name = "Floor")
    private Integer floor;

    @NotBlank(message = "Apartment is mandatory")
    @Column(name = "Apartment")
    private Integer apartment;

    @Size(max = 300, message = "message must be less than 300")
    @Column(name = "Message")
    private String message;
}
