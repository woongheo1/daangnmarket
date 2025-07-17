package com.woong.daangnmarket.post.controller;

import com.woong.daangnmarket.post.dto.PostRequest;
import com.woong.daangnmarket.post.dto.PostResponse;
import com.woong.daangnmarket.post.service.PostServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostServiceImpl postServiceImpl;

    /**
     * 게시글 등록 API
     * 
     * @param request 게시글 등록 요청 데이터
     * @return 생성된 게시글 정보(PostResponse)
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest request) {
        PostResponse response = postServiceImpl.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 게시글 전체 목록 조회 api
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable) {
        Page<PostResponse> posts = postServiceImpl.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 상세 조회 api
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponse response = postServiceImpl.getPostById(postId);
        return ResponseEntity.ok(response);
    }
}
