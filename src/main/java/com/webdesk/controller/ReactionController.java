package com.webdesk.controller;

import com.webdesk.dto.ReactionDTO;
import com.webdesk.entity.Reaction;
import com.webdesk.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleReaction(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        Reaction reaction = reactionService.toggleReaction(userId, postId);
        if (reaction == null) {
            return ResponseEntity.ok().body("Reaction removed");
        }
        return ResponseEntity.ok(new ReactionDTO(reaction));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReactionDTO>> getPostReactions(@PathVariable Long postId) {
        List<ReactionDTO> reactions = reactionService.getPostReactions(postId)
                .stream()
                .map(ReactionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserReacted(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        return ResponseEntity.ok(reactionService.hasUserReacted(userId, postId));
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Integer> getReactionCount(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionCount(postId));
    }

    @DeleteMapping("/{reactionId}")
    public ResponseEntity<Void> deleteReaction(
            @PathVariable Long reactionId,
            @RequestParam Long userId) {
        reactionService.deleteReaction(reactionId, userId);
        return ResponseEntity.ok().build();
    }
}