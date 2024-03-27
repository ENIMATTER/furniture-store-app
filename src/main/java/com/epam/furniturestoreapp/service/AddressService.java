package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public void addAddress(Address address){
        addressRepository.save(address);
    }

    public boolean existsById(long id) {
        return addressRepository.existsById(id);
    }

    public Address findById(long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        addressRepository.deleteById(id);
    }

    public void updateAddress(Address address) {
        addressRepository.save(address);
    }
}
