package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "orderitem")
public class OrderItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orderitemid")
    private Long orderItemID;

    @ManyToOne
    @JoinColumn(name = "ordertableid", referencedColumnName = "ordertableid")
    private OrderTable orderTableID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @NotBlank(message = "Quantity is mandatory")
    @Column(name = "Quantity")
    private Integer quantity;
}
