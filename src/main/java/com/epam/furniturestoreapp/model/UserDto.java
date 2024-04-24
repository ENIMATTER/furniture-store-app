package com.epam.furniturestoreapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private Double balance;
    private String roles;
}
