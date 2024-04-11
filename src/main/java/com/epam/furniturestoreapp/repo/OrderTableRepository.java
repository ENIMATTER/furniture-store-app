package com.epam.furniturestoreapp.repo;

import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> getAllByUserTableID(UserTable userTableID);
}
