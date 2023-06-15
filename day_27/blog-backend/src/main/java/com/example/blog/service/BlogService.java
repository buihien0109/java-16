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

import java.util.ArrayList;
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

    @Transactional
    public BlogPublic createBlog(UpsertBlogRequest request) {
        // TODO: Validate thông tin (nếu cần thiết) - validation

        // Tìm kiếm category
        List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());

        // TODO : Hiện tại fix userId. Sau này userId chính là user đang login
        Integer userId = 1;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Not found user with id = " + userId);
                });

        // Tao blog
        Slugify slugify = Slugify.builder().build();
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .content(request.getContent())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .status(request.getStatus())
                .categories(categories)
                .comments(new ArrayList<>())
                .user(user)
                .build();

        blogRepository.save(blog);
        return BlogPublic.of(blog);
    }

    public BlogPublic getBlogById(Integer id) {
        Blog blog =  blogRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found blog with id = " + id);
        });
        return BlogPublic.of(blog);
    }

    @Transactional
    public BlogPublic updateBlog(Integer id, UpsertBlogRequest request) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found blog with id = " + id);
        });

        // TODO: Validate thông tin (nếu cần thiết) - validation

        // Tìm kiếm category
        List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());

        Slugify slugify = Slugify.builder().build();
        blog.setTitle(request.getTitle());
        blog.setSlug(slugify.slugify(request.getTitle()));
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());
        blog.setThumbnail(request.getThumbnail());
        blog.setCategories(categories);

        blogRepository.save(blog);
        return BlogPublic.of(blog);
    }

    @Transactional
    public void deleteBlog(Integer id) {
        // TODO : Khi xóa blog cẩn thận vì liên quan đến comment và category (có thể sử dụng life cycle để xử lý - PreRemove)
        // Xóa blog -> xóa comment
        // Xóa blog -> Xóa blog-category trong bảng trung gian, không xóa category
        Blog blog = blogRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found blog with id = " + id);
        });

        blogRepository.delete(blog);
    }
}
