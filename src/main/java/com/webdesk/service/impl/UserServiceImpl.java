package com.webdesk.service.impl;

import com.webdesk.dto.JwtResponseDTO;
import com.webdesk.dto.UserDTO;
import com.webdesk.dto.UserRegistrationDTO;
import com.webdesk.entity.User;
import com.webdesk.exception.UnauthorizedException;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;


    @Override
    public UserDTO registerUser(UserRegistrationDTO registrationDTO, MultipartFile profilePicture, MultipartFile coverPicture) throws IOException {
        userRepository.findByUsername(registrationDTO.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("Username already exists");
                });

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setBio(registrationDTO.getBio());
        user.setProfilePictureData(profilePicture.getBytes());
        user.setProfilePictureType(profilePicture.getContentType());
        user.setCoverPictureData(coverPicture.getBytes());
        user.setCoverPictureType(coverPicture.getContentType());

        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserRegistrationDTO registrationDTO,
                              MultipartFile profilePicture, MultipartFile coverPicture) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.getUsername().equals(registrationDTO.getUsername())) {
            userRepository.findByUsername(registrationDTO.getUsername())
                    .ifPresent(existingUser -> {
                        throw new RuntimeException("Username already taken");
                    });
            user.setUsername(registrationDTO.getUsername());
        }

        if (registrationDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        }

        user.setEmail(registrationDTO.getEmail());
        user.setBio(registrationDTO.getBio());

        if (profilePicture != null && !profilePicture.isEmpty()) {
            user.setProfilePictureData(profilePicture.getBytes());
            user.setProfilePictureType(profilePicture.getContentType());
        }

        if (coverPicture != null && !coverPicture.isEmpty()) {
            user.setCoverPictureData(coverPicture.getBytes());
            user.setCoverPictureType(coverPicture.getContentType());
        }

        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser);
    }


    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return new UserDTO(user);
    }

    @Override
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new UserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Users cannot follow themselves");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (following.getFollowers().contains(follower)) {
            throw new RuntimeException("Already following this user");
        }

        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);
    }

    @Override
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        if (!following.getFollowers().contains(follower)) {
            throw new RuntimeException("Not following this user");
        }

        follower.unfollow(following);
        userRepository.save(follower);
        userRepository.save(following);
    }

    @Override
    public List<UserDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.print("Found {"+ user.getFollowers().size() +"} followers for user {"+userId+"}");

        return user.getFollowers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    public List<UserDTO> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getFollowing().size();

        System.out.print("Found {"+ user.getFollowing().size() +"} following for user {"+userId+"}");

        return user.getFollowing().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public JwtResponseDTO verify(UserDTO userDTO) {
        Authentication authentication = authManager.authenticate(
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
        throw new RuntimeException("Invalid credentials");
    }

    public JwtResponseDTO verifyAndRefreshToken(String token) {
        try {
            if (!jwtService.validateToken(token)) {
                throw new UnauthorizedException("Invalid token");
            }

            String username = jwtService.extractUsername(token);
            UserDTO userDTO = findByUsername(username);

            if (userDTO == null) {
                throw new UnauthorizedException("User not found");
            }

            if (jwtService.shouldRefreshToken(token)) {
                String newToken = jwtService.generateToken(username);
                return new JwtResponseDTO(newToken, userDTO);
            }

            return new JwtResponseDTO(token, userDTO);

        } catch (Exception e) {
            throw new UnauthorizedException("Session verification failed");
        }
    }
}