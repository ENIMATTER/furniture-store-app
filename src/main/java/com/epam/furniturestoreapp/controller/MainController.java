package com.epam.furniturestoreapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/account")
    public String account(){
        return "account";
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(){
        return "checkout";
    }

    @GetMapping("/edit-address")
    public String editAddress(){
        return "edit-address";
    }

    @GetMapping("/edit-user")
    public String editUser(){
        return "edit-user";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/not-found")
    public String notFound(){
        return "not-found";
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }

    @GetMapping("/shop-detail")
    public String shopDetail(){
        return "shop-detail";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

}
