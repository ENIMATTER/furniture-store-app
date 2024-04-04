package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Repository
public interface UserTableRepository extends JpaRepository<UserTable, Long> {
    boolean existsByEmail(String email);
    UserTable getByEmail(String email);
}
