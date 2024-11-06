package com.webdesk.service.impl;

import com.webdesk.entity.Post;
import com.webdesk.entity.User;
import com.webdesk.repository.PostRepository;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(MultipartFile image, String caption, Long userId) throws IOException {
        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Handle file upload
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create and save post
        Post post = new Post();
        post.setImageUrl("/uploads/" + uniqueFileName);
        post.setCaption(caption);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getUserPosts(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // Delete the image file
        try {
            Path imagePath = Paths.get("." + post.getImageUrl());
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            // Log the error but continue with database deletion
            System.err.println("Could not delete image file: " + e.getMessage());
        }

        postRepository.delete(post);
    }
}
