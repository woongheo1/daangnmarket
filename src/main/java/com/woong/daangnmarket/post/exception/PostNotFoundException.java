package com.woong.daangnmarket.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String postId) {
        super("해당 ID의 게시글을 찾을 수 없습니다: " + postId);
    }
}
