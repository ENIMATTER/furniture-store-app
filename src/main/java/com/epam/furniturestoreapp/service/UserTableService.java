package com.epam.furniturestoreapp.service;

import com.epam.furniturestoreapp.entity.UserTable;
import com.epam.furniturestoreapp.repo.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTableService implements UserDetailsService {

    @Autowired
    private UserTableRepository userTableRepository;

    public void addUser(UserTable user) {
        userTableRepository.save(user);
    }

    public UserTable getUserById(long id) {
        return userTableRepository.findById(id).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return userTableRepository.existsByEmail(email);
    }

    public UserTable getUserByEmail(String email) {
        return userTableRepository.getByEmail(email).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserTable> user = userTableRepository.getByEmail(username);
        if(user.isPresent()){
            UserTable userObj = user.get();
            return User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getUserPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    private String[] getRoles(UserTable userObj) {
        if (userObj.getRoles() == null) {
            return new String[]{"USER"};
        }
        return userObj.getRoles().split(",");
    }

    public void editUser(UserTable user) {
        userTableRepository.save(user);
    }

    public void deleteUser(UserTable user) {
        userTableRepository.delete(user);
    }

    public List<UserTable> getAll() {
        return userTableRepository.findAll();
    }

    public void deleteUserById(Long userTableID) {
        userTableRepository.deleteById(userTableID);
    }
}
