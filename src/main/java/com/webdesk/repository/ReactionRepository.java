package com.webdesk.repository;

import com.webdesk.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByPostId(Long postId);
    Optional<Reaction> findByUserIdAndPostId(Long userId, Long postId);
}