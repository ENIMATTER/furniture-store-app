package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.repo.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTableService {
    private final UserTableRepository userTableRepository;

    @Autowired
    public UserTableService(UserTableRepository userTableRepository){
        this.userTableRepository = userTableRepository;
    }

    public void addUser(UserTable user) {
        userTableRepository.save(user);
    }

    public boolean existsById(long id) {
        return userTableRepository.existsById(id);
    }

    public UserTable getUserById(long id) {
        return userTableRepository.findById(id).orElse(null);
    }

    public void updateUser(UserTable user) {
        userTableRepository.save(user);
    }

    public void deleteById(long id) {
        userTableRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userTableRepository.existsByEmail(email);
    }

    public UserTable getUserByEmail(String email) {
        return userTableRepository.getByEmail(email);
    }
}
