package com.example.parks.service.interfaces;

import com.example.parks.exceptions.EmailExistsException;
import com.example.parks.exceptions.UserNotFoundException;
import com.example.parks.exceptions.UsernameExistsException;
import com.example.parks.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User register(User user) throws UsernameExistsException, EmailExistsException, UserNotFoundException;

    User getCurrentUser();

    ResponseEntity<String> verifyAccount(String token);

    List<User> getAllUsers();
}
