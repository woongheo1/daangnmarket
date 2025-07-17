package com.woong.daangnmarket.post.service;

import com.woong.daangnmarket.post.dto.PostRequest;
import com.woong.daangnmarket.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponse createPost(PostRequest request);

    Page<PostResponse> getAllPosts(Pageable pageable);

}
