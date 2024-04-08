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
@Table(name = "cartitem")
public class CartItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cartitemID")
    private long cartItemID;

    @ManyToOne
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private UserTable userTableID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @NotBlank(message = "Quantity is mandatory")
    @Column(name = "Quantity")
    private int quantity;
}
