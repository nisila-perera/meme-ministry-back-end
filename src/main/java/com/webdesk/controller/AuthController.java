package com.webdesk.controller;

import com.webdesk.dto.JwtResponseDTO;
import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody UserDTO user) {
        return new ResponseEntity<>(userService.verify(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.registerUser(registrationDTO), HttpStatus.CREATED);
    }
}
