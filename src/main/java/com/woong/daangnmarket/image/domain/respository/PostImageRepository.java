package com.woong.daangnmarket.image.domain.respository;

import com.woong.daangnmarket.image.domain.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findAllByPostId(Long postId);
}
