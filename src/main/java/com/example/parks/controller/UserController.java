package com.example.parks.controller;

import com.example.parks.exceptions.EmailExistsException;
import com.example.parks.exceptions.UserNotFoundException;
import com.example.parks.exceptions.UsernameExistsException;
import com.example.parks.jwt.JWTTokenProvider;
import com.example.parks.model.User;
import com.example.parks.model.UserPrincipal;
import com.example.parks.repository.UserRepository;
import com.example.parks.service.interfaces.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.parks.constant.SecurityConstants.EXPIRATION_TIME;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public User register(@RequestBody User user) throws UsernameExistsException, UserNotFoundException, EmailExistsException {
        return userService.register(user);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestBody User user) throws Exception {

        User loggedUser = userRepository.findByEmail(user.getEmail());
        authenticate(loggedUser.getEmail(), loggedUser.getPassword());
        UserPrincipal userPrincipal = new UserPrincipal(loggedUser);

        loggedUser.setPassword(StringUtils.EMPTY);
        loggedUser.setExpiresIn(EXPIRATION_TIME);
        loggedUser.setToken(jwtTokenProvider.generateJwtToken(userPrincipal));

        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @GetMapping("/verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        return userService.verifyAccount(token);
    }
}
