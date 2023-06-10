package com.example.blog.service;

import com.example.blog.dto.projection.BlogPublic;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Category;
import com.example.blog.entity.User;
import com.example.blog.exception.NotFoundException;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.request.UpsertBlogRequest;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    public Page<BlogPublic> getAllBlog(Integer page, Integer pageSize) {
        Page<BlogPublic> pageInfo = blogRepository.findBlogs(PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending()));
        return pageInfo;
    }

    public Page<BlogPublic> getAllOwnBlog(Integer page, Integer pageSize) {
        // TODO : Hiện tại fix userId. Sau này userId chính là user đang login
        Integer userId = 1;

        Page<BlogPublic> pageInfo = blogRepository.findByUser_IdOrderByCreatedAtDesc(
                userId,
                PageRequest.of(page - 1, pageSize)
        );

        return pageInfo;
    }

    public List<BlogPublic> getAllOwnBlog() {
        // TODO : Hiện tại fix userId. Sau này userId chính là user đang login
        Integer userId = 1;

        List<BlogPublic> pageInfo = blogRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        return pageInfo;
    }
}
