package com.programming.service.impl;

import com.programming.entity.Product;
import com.programming.repository.ProductRepository;
import com.programming.service.ImageService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ProductRepository productRepository;

    public ImageServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void uploadImage(MultipartFile image, Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = "IMG" + System.currentTimeMillis() + ".png";
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            product.setImageUrl(filePath.toString());
            productRepository.save(product);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    @Override
    public File downloadImage(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String imageUrl = product.getImageUrl();
        Path imagePath = Paths.get(imageUrl);

        if (!Files.exists(imagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        return imagePath.toFile();
    }
}
