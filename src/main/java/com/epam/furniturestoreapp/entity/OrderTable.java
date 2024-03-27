package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "orderTable")
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orderTableID")
    private long orderTableID;

    @ManyToOne
    @JoinColumn(name = "userTableID", referencedColumnName = "userTableID")
    private UserTable userTableID;

    @NotBlank(message = "Order date is mandatory")
    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @NotBlank(message = "Total amount is mandatory")
    @Column(name = "TotalAmount")
    private double totalAmount;
}
