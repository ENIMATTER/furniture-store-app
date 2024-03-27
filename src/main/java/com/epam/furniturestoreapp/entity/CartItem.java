package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "cartItem")
public class CartItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CartItemID")
    private long cartItemID;

    @ManyToOne
    @JoinColumn(name = "cartID", referencedColumnName = "cartID")
    private Cart cartID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @NotBlank(message = "Quantity is mandatory")
    @Column(name = "Quantity")
    private int quantity;
}
