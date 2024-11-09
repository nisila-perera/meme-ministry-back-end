package com.webdesk.service;

import com.webdesk.dto.JwtResponseDTO;
import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserRegistrationDTO registrationDTO);
    User getUser(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, UserRegistrationDTO registrationDTO);
    JwtResponseDTO verify(UserDTO user);
    UserDTO findByUsername(String username);
}
