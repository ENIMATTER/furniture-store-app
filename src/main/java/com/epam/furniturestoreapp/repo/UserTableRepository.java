package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTableRepository extends JpaRepository<UserTable, Long> {
    boolean existsByEmail(String email);
    Optional<UserTable> getByEmail(String email);
}
