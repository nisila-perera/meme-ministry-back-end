package com.webdesk.service;

import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;

import java.util.List;

public interface UserService {
    public User registerUser(UserRegistrationDTO registrationDTO);
    public User getUser(Long id);
    public List<User> getAllUsers();
}
