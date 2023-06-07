package com.example.blog.service;

import com.example.blog.dto.projection.BlogPublic;
import com.example.blog.dto.projection.CategoryWebPublic;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Category;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WebService {
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    // Lấy ds blog public được sắp xếp theo thời gian public giảm dần (có phân trang)
    public Page<BlogPublic> getAllBlog(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return blogRepository.findByStatusOrderByPublishedAtDesc(pageRequest, true);
    }

    // Tìm kiếm bài viết theo từ khóa
    public List<BlogPublic> searchBlog(String term) {
        List<Blog> blogList = blogRepository.findByTitleContainsIgnoreCaseAndStatusOrderByPublishedAtDesc(term, true);

        return blogList.stream()
                .map(blog -> BlogPublic.of(blog))
                .toList();
    }

    // Lấy ds category + số bài viết áp dụng category đó
    public List<CategoryWebPublic> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(CategoryWebPublic::of)
                .filter(categoryWebPublic -> categoryWebPublic.getUsed() > 0)
                .toList();
    }

    // Lấy ds 5 category được áp dụng nhiều nhất
    public List<CategoryWebPublic> getTop5Category() {
        List<CategoryWebPublic> categoryWebPublicList = getAllCategory();
        return categoryWebPublicList.stream()
                .sorted((c1, c2) -> c2.getUsed() - (c1.getUsed()))
                .limit(5)
                .toList();
    }

    // Lấy ds blog thuộc category
    public List<BlogPublic> getBlogsOfCategory(String categoryName) {
        List<Blog> blogList = blogRepository.findByCategories_NameAndStatusOrderByPublishedAtDesc(categoryName, true);

        return blogList.stream()
                .map(blog -> BlogPublic.of(blog))
                .toList();
    }

    // Lấy chi tiết bài viết
    public BlogPublic getBlogDetail(Integer blogId, String blogSlug) {
        Blog blog = blogRepository.findByIdAndSlugAndStatus(blogId, blogSlug, true).orElseThrow(() -> {
            throw new RuntimeException(String.format("Not found blog with id = %d and slug = %s", blogId, blogSlug));
        });
        return BlogPublic.of(blog);
    }
}
