package com.webdesk.service.impl;

import com.webdesk.dto.JwtResponseDTO;
import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        registrationDTO.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        registrationDTO.setUsername(registrationDTO.getUsername());
        registrationDTO.setBio(registrationDTO.getBio());
        registrationDTO.setEmail(registrationDTO.getEmail());
        User user = userRepository.save(mapper.map(registrationDTO, User.class));
        return mapper.map(user, UserDTO.class);
    }

    public User updateUser(Long id, UserRegistrationDTO registrationDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setEmail(registrationDTO.getEmail());
        user.setBio(registrationDTO.getBio());

        return userRepository.save(user);
    }

    public UserDTO findByUsername(String username){
        return new UserDTO(userRepository.findByUsername(username).get());
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public JwtResponseDTO verify(UserDTO userDTO) {
        Authentication authentication= authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userDTO.getUsername());
            UserDTO user = findByUsername(userDTO.getUsername());
            return new JwtResponseDTO(token, user);
        }
        throw new RuntimeException("Invalid access");
    }
}
