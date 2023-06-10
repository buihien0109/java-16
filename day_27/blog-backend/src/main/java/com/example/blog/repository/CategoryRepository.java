package com.example.blog.repository;

import com.example.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByIdIn(List<Integer> ids);
}