package com.woong.daangnmarket.post.service;

import com.woong.daangnmarket.post.dto.PostRequest;
import com.woong.daangnmarket.post.dto.PostResponse;
import com.woong.daangnmarket.post.dto.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest request);

    Page<PostResponse> getAllPosts(Pageable pageable);

    PostResponse updatePost(Long postId, PostUpdateRequest request);

    PostResponse getPostById(Long postId);

    List<PostResponse> getPostsByLocation(double latitude, double longitude, double radius);

    void deletePost(Long postId);



}
