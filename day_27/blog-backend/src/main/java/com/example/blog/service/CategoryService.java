package com.example.blog.service;

import com.example.blog.dto.projection.CategoryPublic;
import com.example.blog.entity.Category;
import com.example.blog.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryPublic> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(category -> CategoryPublic.of(category))
                .collect(Collectors.toList());
    }
}
