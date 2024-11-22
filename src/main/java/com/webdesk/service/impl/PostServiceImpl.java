package com.webdesk.service.impl;

import com.webdesk.dto.PostDTO;
import com.webdesk.entity.Post;
import com.webdesk.entity.User;
import com.webdesk.repository.PostRepository;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(MultipartFile image, String caption, Long userId) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        Post post = new Post();
        post.setImageData(image.getBytes());
        post.setImageType(image.getContentType());
        post.setCaption(caption);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        return postRepository.save(post);
    }


    @Override
    public List<PostDTO> getUserFollowingPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<User> following = user.getFollowing();
        List<Long> followingIds = following.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findByUserIdInOrderByCreatedAtDesc(followingIds);
        return posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Post updatePost(Long postId, MultipartFile newImage, String newCaption) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (newCaption != null) {
            post.setCaption(newCaption);
        }

        if (newImage != null && !newImage.isEmpty()) {
            post.setImageData(newImage.getBytes());
            post.setImageType(newImage.getContentType());
        }

        return postRepository.save(post);
    }
}
