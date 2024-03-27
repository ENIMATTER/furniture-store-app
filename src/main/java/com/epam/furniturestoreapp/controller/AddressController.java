package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @GetMapping
    public String getAllAddresses(Model model) {
        List<Address> addresses = addressService.findAll();
        model.addAttribute("addresses", addresses);
        return "";
    }

    @GetMapping("/add")
    public String getAddAddress(Model model) {
        Address address = new Address();
        model.addAttribute("address", address);
        return "";
    }

    @PostMapping("/add")
    public String postAddAddress(@Valid @ModelAttribute("address") Address address,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "";
        }
        addressService.addAddress(address);
        return "";
    }

    @GetMapping("/{id}")
    public String getAddress(@PathVariable(value = "id") long id, Model model) {
        if (!addressService.existsById(id)) {
            return "";
        }
        Address address = addressService.findById(id);
        model.addAttribute("address", address);
        return "";
    }

    @GetMapping("/{id}/edit")
    public String getEditAddress(@PathVariable(value = "id") long id, Model model) {
        if (!addressService.existsById(id)) {
            return "";
        }
        Address address = addressService.findById(id);
        model.addAttribute("address", address);
        return "";
    }

    @PutMapping("/{id}/edit")
    public String putEditAddress(@PathVariable(value = "id") long id,
                                 @Valid @ModelAttribute("address") Address address,
                                 BindingResult bindingResult) {
        if (!addressService.existsById(id)) {
            return "";
        }
        if (bindingResult.hasErrors()) {
            return "";
        }
        Address newAddress = addressService.findById(id);
        newAddress.setStreet(address.getStreet());
        newAddress.setCity(address.getCity());
        newAddress.setState(address.getState());
        newAddress.setCountry(address.getCountry());
        newAddress.setZipCode(address.getZipCode());
        addressService.updateAddress(newAddress);
        return "";
    }

    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable(value = "id") long id) {
        if (!addressService.existsById(id)) {
            return "";
        }
        addressService.deleteById(id);
        return "";
    }
}
