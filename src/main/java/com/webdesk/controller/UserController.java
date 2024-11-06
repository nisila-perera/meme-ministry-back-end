package com.webdesk.controller;

import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        User user = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
