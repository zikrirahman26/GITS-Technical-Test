package com.programming.controller;

import com.programming.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/products")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("{productId}/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile multipartFile, @PathVariable Long productId) {
        imageService.uploadImage(multipartFile, productId);
        return new ResponseEntity<>("Successfully Uploaded Image", HttpStatus.OK);
    }

    @GetMapping("{productId}/download-image")
    public ResponseEntity<?> downloadImage(@PathVariable Long productId) throws FileNotFoundException {
        File image = imageService.downloadImage(productId);
        FileInputStream fileInputStream = new FileInputStream(image);
        MediaType mediaType = MediaType.IMAGE_PNG;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + image.getName() + "\"");
        headers.setContentType(mediaType);
        return new ResponseEntity<>(new InputStreamResource(fileInputStream), headers, HttpStatus.OK);
    }
}
