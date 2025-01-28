package com.programming.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ImageService {

    void uploadImage(MultipartFile image, Long productId);

    File downloadImage(Long productId);
}
