package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "cart")
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CartID")
    private long cartID;

    @ManyToOne
    @JoinColumn(name = "userTableID", referencedColumnName = "userTableID")
    private UserTable userTableID;
}
