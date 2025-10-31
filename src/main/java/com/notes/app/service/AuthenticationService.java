package com.notes.app.service;

import com.notes.app.domain.User;
import com.notes.app.dto.auth.AuthenticationRequest;
import com.notes.app.dto.auth.AuthenticationResponse;
import com.notes.app.dto.auth.RegisterRequest;
import com.notes.app.exception.UserAlreadyExistFound;
import com.notes.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).build();

        User foundUser = userRepository.findByEmail(request.getEmail());

        if (foundUser != null) {
            throw new UserAlreadyExistFound("User with this email already exists");
        }

        User savedUser = userRepository.save(newUser);

        String token = jwtService.generateToken(savedUser);

        return AuthenticationResponse.builder().token(token).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User foundUser = userRepository.findByEmail(request.getEmail());

        if (foundUser == null) {
            throw new UsernameNotFoundException(request.getEmail());
        }

        String token = jwtService.generateToken(foundUser);

        return AuthenticationResponse.builder().token(token).build();
    }
}
