package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "ordertable")
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ordertableid")
    private Long orderTableID;

    @ManyToOne
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private UserTable userTableID;

    @NotBlank(message = "Order date is mandatory")
    @Column(name = "orderdate")
    private LocalDateTime orderDate;

    @NotBlank(message = "Total amount is mandatory")
    @Column(name = "totalamount")
    private Double totalAmount;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "ordertableid", referencedColumnName = "ordertableid")
    private Address address;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "ordertableid", referencedColumnName = "ordertableid")
    private List<OrderItem> orderItems;
}
