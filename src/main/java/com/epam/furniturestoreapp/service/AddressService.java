package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.entity.UserTable;
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

    public Address getByUserTableID(UserTable userTableID){
        return addressRepository.getByUserTableID(userTableID).orElse(new Address());
    }

    public Address getAddressById(Long addressID) {
        return addressRepository.findById(addressID).orElse(null);
    }

    public void editAddress(Address address) {
        addressRepository.save(address);
    }

    public void addAddress(Address address) {
        addressRepository.save(address);
    }

    public void deleteAddress(Address address) {
        addressRepository.delete(address);
    }
}
