package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "Quantity is mandatory")
    @Column(name = "Quantity")
    private Integer quantity;
}
