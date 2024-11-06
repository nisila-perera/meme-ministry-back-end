package com.webdesk.dto;

import com.webdesk.entity.Reaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long postId;
    private LocalDateTime createdAt;

    public ReactionDTO(Reaction reaction) {
        this.id = reaction.getId();
        this.userId = reaction.getUser().getId();
        this.username = reaction.getUser().getUsername();
        this.postId = reaction.getPost().getId();
        this.createdAt = reaction.getCreatedAt();
    }
}