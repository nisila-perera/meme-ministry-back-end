package com.webdesk.controller;

import com.webdesk.dto.CommentDTO;
import com.webdesk.entity.Comment;
import com.webdesk.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestParam Long userId,
            @RequestParam Long postId,
            @RequestBody String content) {
        CommentDTO commentDTO = new CommentDTO(commentService.createComment(userId, postId, content));
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestBody String content) {
        CommentDTO commentDTO = new CommentDTO(commentService.updateComment(commentId, userId, content));
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getPostComments(postId);
        List<CommentDTO> commentDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            commentDTOs.add(new CommentDTO(comment));
        }

        return ResponseEntity.ok(commentDTOs);
    }
}