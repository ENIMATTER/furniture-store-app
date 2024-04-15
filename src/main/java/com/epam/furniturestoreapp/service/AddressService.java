package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public void addAddress(Address address) {
        addressRepository.save(address);
    }
}
