package com.webdesk.service;

import com.webdesk.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long userId, Long postId, String content);
    void deleteComment(Long commentId, Long userId);
    Comment updateComment(Long commentId, Long userId, String content);
    List<Comment> getPostComments(Long postId);
}