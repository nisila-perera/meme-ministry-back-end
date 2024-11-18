package com.webdesk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String bio;

    @Lob
    @Column(name = "profile_picture_data", columnDefinition = "LONGBLOB")
    private byte[] profilePictureData;

    private String profilePictureType;

    @Lob
    @Column(name = "cover_picture_data", columnDefinition = "LONGBLOB")
    private byte[] coverPictureData;

    private String coverPictureType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    public void follow(User userToFollow) {
        userToFollow.getFollowers().add(this);
        this.getFollowing().add(userToFollow);
    }

    public void unfollow(User userToUnfollow) {
        userToUnfollow.getFollowers().remove(this);
        this.getFollowing().remove(userToUnfollow);
    }
}