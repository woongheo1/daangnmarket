package com.woong.daangnmarket.image.service;

import com.woong.daangnmarket.image.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Uploader s3Uploader;

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
}
