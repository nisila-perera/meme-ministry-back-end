package com.webdesk.dto;

import com.webdesk.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String bio;
    private byte[] profilePictureData;
    private String profilePictureType;
    private byte[] coverPictureData;
    private String coverPictureType;
    private int postCount;
    private int followerCount;
    private int followingCount;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.profilePictureData = user.getProfilePictureData();
        this.profilePictureType = user.getProfilePictureType();
        this.coverPictureData = user.getCoverPictureData();
        this.coverPictureType = user.getCoverPictureType();
        this.postCount = user.getPosts() != null ? user.getPosts().size() : 0;
        this.followerCount = user.getFollowers() != null ? user.getFollowers().size() : 0;
        this.followingCount = user.getFollowing() != null ? user.getFollowing().size() : 0;
    }
}