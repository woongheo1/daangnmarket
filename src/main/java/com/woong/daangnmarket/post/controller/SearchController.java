package com.woong.daangnmarket.post.controller;


import com.woong.daangnmarket.post.dto.PostResponse;
import com.woong.daangnmarket.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class SearchController {

    private final PostServiceImpl postServiceImpl;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<PostResponse>> getPostsByCategory(
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostResponse> posts = postServiceImpl.getPostsByCategory(categoryId, pageable);
        return ResponseEntity.ok(posts);
    }

}


