package com.woong.daangnmarket.post.service;


import com.woong.daangnmarket.member.domain.Member;

import com.woong.daangnmarket.member.domain.repository.MemberRepository;
import com.woong.daangnmarket.post.domain.entity.Category;
import com.woong.daangnmarket.post.domain.entity.Location;
import com.woong.daangnmarket.post.domain.entity.Post;
import com.woong.daangnmarket.post.domain.repository.CategoryRepository;
import com.woong.daangnmarket.post.domain.repository.PostRepository;
import com.woong.daangnmarket.post.dto.PostRequest;
import com.woong.daangnmarket.post.dto.PostResponse;

import com.woong.daangnmarket.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        // 작성자 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보가 존재하지 않습니다."));

        // 카테고리 조회 (null 가능)
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리 정보가 존재하지 않습니다."));
        }

        // Location 객체 생성 (latitude, longitude 둘 다 있을 때만)
        Location location = null;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            location = Location.builder()
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .build();
        }

        // 게시글 생성
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .region(request.getRegion())
                .status(request.getStatus())
                .imageUrl(request.getImageUrl())
                .location(location)
                .member(member)
                .category(category)
                .build();

        // 저장
        Post saved = postRepository.save(post);

        // 응답 DTO 반환
        return new PostResponse(saved);
    }
    //  전체 게시글 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAllByRemovedFalse(pageable)
                .map(PostResponse::new);
    }
    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 없습니다."));

        return PostResponse.from(post);
    }
    // ✅ 카테고리 기반 게시글 조회
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryIdAndRemovedFalse(categoryId, pageable)
                .map(PostResponse::new);
    }

    // 위치 기반 메서드 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByLocation(double latitude, double longitude, double radius) {
        List<Post> posts = postRepository.findPostsByLocationWithinRadius(latitude, longitude, radius);
        return posts.stream()
                .map(PostResponse::new)
                .toList();
    }

}
