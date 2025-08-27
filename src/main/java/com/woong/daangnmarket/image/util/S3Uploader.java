package com.woong.daangnmarket.image.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드 후 URL 반환
     * @param file 업로드할 MultipartFile
     * @param dirName S3 내 디렉토리 경로 (예: "post-images")
     * @return 업로드된 파일의 S3 URL
     * @throws IOException
     */
    public String upload(MultipartFile file, String dirName) throws IOException {
        // 유니크 파일명 생성 (중복 방지)
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

        // 업로드된 파일 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * S3에서 파일 삭제
     * @param fileUrl 삭제할 파일의 전체 URL
     */
    public void delete(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        amazonS3.deleteObject(bucket, fileName);
    }

    /**
     * URL에서 파일 이름 추출
     * @param fileUrl 파일의 전체 URL
     * @return S3 버킷 내의 파일 이름 (경로 포함)
     */
    private String extractFileNameFromUrl(String fileUrl) {
        try {
            java.net.URL url = new java.net.URL(fileUrl);
            return url.getPath().substring(1);
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Invalid file URL", e);
        }
    }
}