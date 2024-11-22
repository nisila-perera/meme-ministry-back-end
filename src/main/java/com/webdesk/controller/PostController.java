package com.webdesk.controller;

import com.webdesk.dto.PostDTO;
import com.webdesk.entity.Post;
import com.webdesk.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("image") MultipartFile image,
            @RequestParam("caption") String caption,
            @RequestParam("userId") Long userId) {
        System.out.println("Received");
        try {
            Post createdPost = postService.createPost(image, caption, userId);
            return ResponseEntity.ok(new PostDTO(createdPost));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/user/{userId}/following")
    public ResponseEntity<List<PostDTO>> getFollowingUserPosts(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getUserFollowingPosts(userId);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestParam(required = false) MultipartFile newImage,
            @RequestParam(required = false) String newCaption) throws IOException {
        Post updatedPost = postService.updatePost(postId, newImage, newCaption);
        return ResponseEntity.ok(new PostDTO(updatedPost));
    }
}

