package com.woong.daangnmarket.image.controller;

import com.woong.daangnmarket.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    /**
     * 이미지 업로드 API
     * @param file 업로드할 MultipartFile
     * @return 업로드된 이미지 URL 반환
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    /**
     * 이미지 삭제 API
     * @param imageUrl 삭제할 이미지 URL
     * @return 성공 메시지
     */
    @DeleteMapping
    public ResponseEntity<String> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        imageService.deleteImage(imageUrl);
        return ResponseEntity.ok("Image deleted successfully");
    }
}
