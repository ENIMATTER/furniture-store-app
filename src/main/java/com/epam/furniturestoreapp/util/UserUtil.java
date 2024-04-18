package com.epam.furniturestoreapp.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserUtil {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private Double balance;
    private String roles;
}
