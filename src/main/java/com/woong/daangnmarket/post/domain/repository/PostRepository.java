package com.woong.daangnmarket.post.domain.repository;


import com.woong.daangnmarket.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.id = :postId AND p.removed = false")
    public Optional<Post> findPostById(Long postId);

    // soft delete 처리된 게시글 제외하고 조회
    Page<Post> findAllByRemovedFalse(Pageable pageable);
}
