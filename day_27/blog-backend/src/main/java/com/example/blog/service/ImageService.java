package com.example.blog.service;

import com.example.blog.entity.Image;
import com.example.blog.entity.User;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.NotFoundException;
import com.example.blog.repository.ImageRepository;
import com.example.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public Image uploadFile(MultipartFile file) {
        validateFile(file);

        try {
            // TODO : Hiện tại fix userId. Sau này userId chính là user đang login
            Integer userId = 1;
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Not found user with id = " + userId);
                    });
            Image image = Image.builder()
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .user(user)
                    .build();
            imageRepository.save(image);

            return image;
        } catch (IOException e) {
            throw new RuntimeException("Có lỗi xảy ra");
        }
    }

    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // Tên file không được trống
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        // Type file có nằm trong ds cho phép hay không
        // avatar.png, image.jpg => png, jpg
        String fileExtension = getFileExtension(fileName);
        if (!checkFileExtension(fileExtension)) {
            throw new BadRequestException("Type file không hợp lệ");
        }

        // Kích thước size có trong phạm vi cho phép không
        double fileSize = (double) (file.getSize() / 1_048_576);
        if (fileSize > 2) {
            throw new BadRequestException("Size file không được vượt quá 2MB");
        }
    }

    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }

    public boolean checkFileExtension(String fileExtension) {
        List<String> fileExtensions = List.of("png", "jpg", "jpeg");
        return fileExtensions.contains(fileExtension);
    }

    public Image readFile(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found image");
                });
        return image;
    }

    public void deleteFile(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found image");
                });
        imageRepository.delete(image);
    }

    public List<Image> getFilesOfUser() {
        // TODO : Hiện tại fix userId. Sau này userId chính là user đang login
        Integer userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found user with id = " + userId);
                });

        return imageRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }
}
