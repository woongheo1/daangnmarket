package com.woong.daangnmarket.post.domain.repository;


import com.woong.daangnmarket.post.domain.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.id = :postId AND p.removed = false")
    public Optional<Post> findPostById(Long postId);

    // 카테고리 ID로 게시글 검색 (페이징 처리 포함)
    Page<Post> findByCategoryIdAndRemovedFalse(Long categoryId, Pageable pageable);

    // soft delete 처리된 게시글 제외하고 조회
    Page<Post> findAllByRemovedFalse(Pageable pageable);
    // 위치 기반 게시글 조회 - native query (반경 내 게시글 찾기)
    @Query(value = """
        SELECT p.*
        FROM post p
        JOIN location l ON p.LOCATION_ID = l.LOCATION_ID
        WHERE p.IS_REMOVED = false
          AND (
            6371 * acos(
              cos(radians(:latitude)) * cos(radians(l.latitude)) *
              cos(radians(l.longitude) - radians(:longitude)) +
              sin(radians(:latitude)) * sin(radians(l.latitude))
            )
          ) <= :radiusKm
        ORDER BY (
            6371 * acos(
              cos(radians(:latitude)) * cos(radians(l.latitude)) *
              cos(radians(l.longitude) - radians(:longitude)) +
              sin(radians(:latitude)) * sin(radians(l.latitude))
            )
          )
        """, nativeQuery = true)
    List<Post> findPostsByLocationWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusKm") double radiusKm);
}