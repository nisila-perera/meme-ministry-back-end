package com.webdesk.controller;

import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserPrincipal;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestPart UserRegistrationDTO registrationDTO,
                                        @RequestPart(required = false) MultipartFile profilePicture,
                                        @RequestPart(required = false) MultipartFile coverPicture) {
        try {
            UserDTO updatedUser = userService.updateUser(id, registrationDTO, profilePicture, coverPicture);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException | IOException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userService.followUser(userPrincipal.getId(), userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userService.unfollowUser(userPrincipal.getId(), userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        try {
            List<UserDTO> followers = userService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
        try {
            List<UserDTO> following = userService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return new ResponseEntity<>(response, status);
    }
}