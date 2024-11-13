package com.webdesk.service.impl;

import com.webdesk.entity.Post;
import com.webdesk.entity.User;
import com.webdesk.repository.PostRepository;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.PostService;
import com.webdesk.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(MultipartFile image, String caption, Long userId) throws IOException {
        // Validate input
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Validate file type
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Create new post
        Post post = new Post();
        post.setImageData(FileUtils.compressImage(image.getBytes()));
        post.setImageType(contentType);
        post.setCaption(caption);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        return postRepository.save(post);
    }

    @Override
    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getUserPosts(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        postRepository.delete(post);
    }

    // Add method to get decompressed image data
    public byte[] getPostImage(Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        return post.getImageData() != null ? FileUtils.decompressImage(post.getImageData()) : null;
    }

    // Add method to update post
    public Post updatePost(Long postId, MultipartFile newImage, String newCaption) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (newImage != null && !newImage.isEmpty()) {
            String contentType = newImage.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image");
            }
            post.setImageData(FileUtils.compressImage(newImage.getBytes()));
            post.setImageType(contentType);
        }

        if (newCaption != null) {
            post.setCaption(newCaption);
        }

        return postRepository.save(post);
    }
}