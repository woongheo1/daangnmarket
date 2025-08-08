    package com.woong.daangnmarket.image.dto;

    public class ImageUploadResponse {
        private String imageUrl;

        public ImageUploadResponse(String imageUrl) {

            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {

            return imageUrl;
        }
    }
