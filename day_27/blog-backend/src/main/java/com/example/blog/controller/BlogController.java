package com.example.blog.controller;

import com.example.blog.dto.projection.BlogPublic;
import com.example.blog.dto.projection.CategoryPublic;
import com.example.blog.request.UpsertBlogRequest;
import com.example.blog.service.BlogService;
import com.example.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final CategoryService categoryService;

    // Danh sách tất cả bài viết
    @GetMapping("/admin/blogs")
    public String getBlogPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                              Model model) {
        Page<BlogPublic> pageInfo = blogService.getAllBlog(page, pageSize);
        model.addAttribute("page", pageInfo);
        model.addAttribute("currentPage", page);
        return "admin/blog/blog-index";
    }

    // Danh sách bài viết của tôi
    @GetMapping("/admin/blogs/own-blogs")
    public String getOwnBlogPage(Model model) {
        List<BlogPublic> blogList = blogService.getAllOwnBlog();
        model.addAttribute("blogList", blogList);
        return "admin/blog/own-blog";
    }

    // Tạo bài viết
    @GetMapping("/admin/blogs/create")
    public String getBlogCreatePage(Model model) {
        List<CategoryPublic> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);
        return "admin/blog/blog-create";
    }

    // Chi tiết bài viết
    @GetMapping("/admin/blogs/{id}/detail")
    public String getBlogDetailPage(@PathVariable Integer id, Model model) {
        BlogPublic blog = blogService.getBlogById(id);
        List<CategoryPublic> categoryList = categoryService.getAllCategory();

        model.addAttribute("blog", blog);
        model.addAttribute("categoryList", categoryList);
        return "admin/blog/blog-detail";
    }

    // Danh sách API
    // 1. Tạo bài viết
    @PostMapping("/api/v1/admin/blogs")
    public ResponseEntity<?> createBlog(@RequestBody UpsertBlogRequest request) {
        return new ResponseEntity<>(blogService.createBlog(request), HttpStatus.CREATED); // 201
    }

    // 2. Cập nhật bài viết
    @PutMapping("/api/v1/admin/blogs/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Integer id, @RequestBody UpsertBlogRequest request) {
        return ResponseEntity.ok(blogService.updateBlog(id, request)); // 200
    }

    // 3. Xóa bài viết
    @DeleteMapping("/api/v1/admin/blogs/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
