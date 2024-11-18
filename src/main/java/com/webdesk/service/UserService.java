package com.webdesk.service;

import com.webdesk.dto.JwtResponseDTO;
import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDTO registerUser(UserRegistrationDTO registrationDTO, MultipartFile profilePicture, MultipartFile coverPicture) throws IOException;
    UserDTO updateUser(Long id, UserRegistrationDTO registrationDTO, MultipartFile profilePicture, MultipartFile coverPicture) throws IOException;
    UserDTO findByUsername(String username);
    UserDTO getUser(Long id);
    List<UserDTO> getAllUsers();
    JwtResponseDTO verify(UserDTO userDTO);
    JwtResponseDTO verifyAndRefreshToken(String token);
}