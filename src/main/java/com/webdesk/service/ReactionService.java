package com.webdesk.service;

import com.webdesk.entity.Reaction;

import java.util.List;

public interface ReactionService {
    Reaction toggleReaction(Long userId, Long postId);
    List<Reaction> getPostReactions(Long postId);
    boolean hasUserReacted(Long userId, Long postId);
    void deleteReaction(Long reactionId, Long userId);
    int getReactionCount(Long postId);
}