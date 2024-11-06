package com.webdesk.service.impl;

import com.webdesk.entity.Comment;
import com.webdesk.entity.Post;
import com.webdesk.entity.User;
import com.webdesk.repository.CommentRepository;
import com.webdesk.repository.PostRepository;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public Comment createComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this comment");
        }

        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getPostComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}