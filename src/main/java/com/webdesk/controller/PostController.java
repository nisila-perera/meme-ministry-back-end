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
import java.util.stream.Collectors;


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

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return postService.getPost(id)
                .map(post -> ResponseEntity.ok(new PostDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable Long userId) {
        List<Post> posts = postService.getUserPosts(userId);
        List<PostDTO> postDTOs = posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
