package com.woong.daangnmarket.post.service;

import com.woong.daangnmarket.post.dto.PostRequest;
import com.woong.daangnmarket.post.dto.PostResponse;
import com.woong.daangnmarket.post.dto.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest request, MultipartFile imageFile) throws IOException;

    Page<PostResponse> getAllPosts(Pageable pageable);

    PostResponse updatePost(Long postId, PostUpdateRequest request);

    PostResponse getPostById(Long postId);

    List<PostResponse> getPostsByLocation(double latitude, double longitude, double radius);

    void deletePost(Long postId);



}
