package com.webdesk.dto;

import com.webdesk.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
    private UserDTO user;
    private int commentCount;
    private int reactionCount;

    // Constructor to convert from Entity to DTO
    public PostDTO(Post post) {
        this.id = post.getId();
        this.imageUrl = post.getImageUrl();
        this.caption = post.getCaption();
        this.createdAt = post.getCreatedAt();
        this.user = new UserDTO(post.getUser());
        this.commentCount = post.getComments() != null ? post.getComments().size() : 0;
        this.reactionCount = post.getReactions() != null ? post.getReactions().size() : 0;
    }
}
