package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.Address;
import com.epam.furniturestoreapp.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> getByUserTableID(UserTable userTableID);
}
