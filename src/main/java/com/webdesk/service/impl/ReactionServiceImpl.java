package com.webdesk.service.impl;

import com.webdesk.entity.Post;
import com.webdesk.entity.Reaction;
import com.webdesk.entity.User;
import com.webdesk.repository.PostRepository;
import com.webdesk.repository.ReactionRepository;
import com.webdesk.repository.UserRepository;
import com.webdesk.service.ReactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public Reaction toggleReaction(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Reaction> existingReaction = reactionRepository.findByUserIdAndPostId(userId, postId);

        if (existingReaction.isPresent()) {
            reactionRepository.delete(existingReaction.get());
            return null;
        } else {
            Reaction reaction = new Reaction();
            reaction.setUser(user);
            reaction.setPost(post);
            return reactionRepository.save(reaction);
        }
    }

    @Override
    public List<Reaction> getPostReactions(Long postId) {
        return reactionRepository.findByPostId(postId);
    }

    @Override
    public boolean hasUserReacted(Long userId, Long postId) {
        return reactionRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }

    @Override
    public void deleteReaction(Long reactionId, Long userId) {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        if (!reaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this reaction");
        }

        reactionRepository.delete(reaction);
    }

    @Override
    public int getReactionCount(Long postId) {
        return reactionRepository.findByPostId(postId).size();
    }
}