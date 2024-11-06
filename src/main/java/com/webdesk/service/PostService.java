package com.webdesk.service;

import com.webdesk.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Post createPost(MultipartFile image, String caption, Long userId) throws IOException;
    Optional<Post> getPost(Long id);
    List<Post> getUserPosts(Long userId);
    void deletePost(Long id);
}
