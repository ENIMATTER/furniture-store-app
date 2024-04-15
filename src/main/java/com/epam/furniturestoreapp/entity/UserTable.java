package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "usertable")
public class UserTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "usertableid")
    private Long userTableID;

    @Size(max = 50, message = "Firstname must be less than 50")
    @NotBlank(message = "Firstname is mandatory")
    @Column(name = "Firstname")
    private String firstname;

    @Size(max = 50, message = "Lastname must be less than 50")
    @NotBlank(message = "Lastname is mandatory")
    @Column(name = "Lastname")
    private String lastname;

    @Size(max = 50, message = "Email must be less than 50")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "Email")
    private String email;

    @Size(max = 100, message = "User password must be less than 100")
    @NotBlank(message = "User password is mandatory")
    @Column(name = "userpassword")
    private String userPassword;

    @Size(max = 50, message = "Phone number must be less than 50")
    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phonenumber")
    private String phoneNumber;

    @NotBlank(message = "Balance is mandatory")
    @Column(name = "Balance")
    private Double balance;

    @Size(max = 50, message = "Roles must be less than 50")
    @NotBlank(message = "Roles is mandatory")
    @Column(name = "roles")
    private String roles;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private List<OrderTable> orderTables;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private List<CartItem> cartItems;
}
