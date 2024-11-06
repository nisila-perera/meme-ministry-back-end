package com.webdesk.service.impl;

import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword()); // In real app, encrypt this!
        user.setEmail(registrationDTO.getEmail());
        user.setBio(registrationDTO.getBio());

        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
