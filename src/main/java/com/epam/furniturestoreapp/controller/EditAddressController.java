package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.service.AddressService;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.UserTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.emailUsername;
import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/edit-address")
public class EditAddressController {
    private final UserTableService userTableService;
    private final AddressService addressService;
    private final List<Category> categories;

    @Autowired
    public EditAddressController(UserTableService userTableService, AddressService addressService,
                                 CategoryService categoryService) {
        this.userTableService = userTableService;
        this.addressService = addressService;
        this.categories = categoryService.findAll();
    }

    @GetMapping
    public String getEditAddress(Model model) {
        UserTable user = userTableService.getUserByEmail(emailUsername);
        Address address = addressService.getByUserTableID(user);
        model.addAttribute("address", address);
        addToModelBasicAttributes(model);
        return "edit-address";
    }

    @PutMapping
    public String putEditAddress(@RequestParam("addressID") Long addressID,
                                 @RequestParam("street") String street,
                                 @RequestParam("city") String city,
                                 @RequestParam("state") String state,
                                 @RequestParam("country") String country,
                                 @RequestParam("zipCode") String zipCode,
                                 Model model) {
        Address address = addressService.getAddressById(addressID);
        if (address == null) {
            return "error-page";
        }
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipCode(zipCode);
        addressService.editAddress(address);
        model.addAttribute("address", address);
        addToModelBasicAttributes(model);
        return "edit-address";
    }

    private void addToModelBasicAttributes(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
    }
}
