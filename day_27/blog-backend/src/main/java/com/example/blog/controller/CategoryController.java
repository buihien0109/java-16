package com.example.blog.controller;

import com.example.blog.dto.projection.BlogPublic;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class CategoryController {
    // Danh sách tất cả bài viết
    @GetMapping("/admin/categories")
    public String getBlogPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                              Model model) {
        return "admin/category/category-list";
    }
}
