package com.example.parks.service;

import com.example.parks.exceptions.EmailExistsException;
import com.example.parks.exceptions.UserNotFoundException;
import com.example.parks.exceptions.UsernameExistsException;
import com.example.parks.jwt.JWTTokenProvider;
import com.example.parks.model.NotificationEmail;
import com.example.parks.model.User;
import com.example.parks.model.UserPrincipal;
import com.example.parks.repository.UserRepository;
import com.example.parks.service.interfaces.UserService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.parks.constant.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.example.parks.constant.ErrorConstants.USERNAME_ALREADY_EXISTS;
import static com.example.parks.constant.SecurityConstants.EXPIRATION_TIME;
import static com.example.parks.model.Role.ROLE_ADMIN;
import static com.example.parks.model.Role.ROLE_USER;

@Service
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }


    @Override
    public User register(User user) throws UsernameExistsException, EmailExistsException, UserNotFoundException {
        validateUsernameAndEmail(user.getUsername(), user.getEmail());

        String verificationToken = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setIsLocked(false);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setToken(verificationToken);

        User registeredUser = userRepository.save(user);
        registeredUser.setPassword(StringUtils.EMPTY);

        mailService.sendMail(new NotificationEmail("Account Activation - Ireland Parks",
                user.getEmail(), "Thank you for signing up to Ireland Parks, " +
                "please click the link below to activate your account " +
                "http://localhost:8080/api/verification/" + verificationToken));

        return registeredUser;
    }

    private void validateUsernameAndEmail(String newUsername, String newEmail) throws UsernameExistsException, EmailExistsException {
        User userByEmail = userRepository.findByEmail(newEmail);
        if(userByEmail != null) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
        User userByUsername = userRepository.findByEmail(newUsername);
        if(userByUsername != null) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
    }

    @Override
    public User getCurrentUser() {
        User principal = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername());
    }

    @Override
    public ResponseEntity<String> verifyAccount(String token) {
        User user = userRepository.findByToken(token);
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<>(new Gson().toJson("Account Activated Successfully"), HttpStatus.OK);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return new UserPrincipal(user);
    }
}
