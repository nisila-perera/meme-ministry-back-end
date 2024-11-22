package com.webdesk.service;

import com.webdesk.dto.PostDTO;
import com.webdesk.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    public Post createPost(MultipartFile image, String caption, Long userId) throws IOException;
    public List<PostDTO> getUserFollowingPosts(Long userId);
    public Post updatePost(Long postId, MultipartFile image, String caption) throws IOException;
}
