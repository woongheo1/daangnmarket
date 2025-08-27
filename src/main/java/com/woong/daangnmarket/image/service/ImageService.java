package com.woong.daangnmarket.image.service;

import com.woong.daangnmarket.image.domain.entity.PostImage;
import com.woong.daangnmarket.image.domain.respository.PostImageRepository;
import com.woong.daangnmarket.image.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final S3Uploader s3Uploader;
    private final PostImageRepository postImageRepository;

    /**
     * 이미지 파일을 S3에 업로드하고 URL 반환
     *
     * @param file 업로드할 MultipartFile
     * @return 업로드된 이미지 URL
     * @throws IOException
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // "post-images"는 S3 내 저장할 폴더명(원하는대로 변경 가능)
        return s3Uploader.upload(file, "post-images");
    }

    /**
     * 이미지 URL을 받아 S3에서 삭제하고 DB에서도 삭제
     * @param imageUrl 삭제할 이미지 URL
     */
    public void deleteImage(String imageUrl) {
        // S3에서 이미지 삭제
        s3Uploader.delete(imageUrl);

        // DB에서 이미지 정보 삭제
        PostImage postImage = postImageRepository.findByImageUrl(imageUrl);
        if (postImage != null) {
            postImageRepository.delete(postImage);
        }
    }
}
