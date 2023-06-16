package com.example.blog.controller;

import com.example.blog.entity.Image;
import com.example.blog.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/files")
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    // 1. Lấy danh sách file của user đang login
    @GetMapping()
    public ResponseEntity<?> deleteFile() {
        List<Image> imageList = imageService.getFilesOfUser();
        return ResponseEntity.ok(imageList);
    }

    // 2. Upload file
    @PostMapping("")
    public ResponseEntity<?> uploadFile(@ModelAttribute("file") MultipartFile file) {
        Image image = imageService.uploadFile(file);
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    // 3. Xem thông tin file
    @GetMapping("{id}")
    public ResponseEntity<?> readFile(@PathVariable Integer id) {
        Image image = imageService.readFile(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .body(image.getData());
    }

    // 4. Xóa file
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer id) {
        imageService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}

