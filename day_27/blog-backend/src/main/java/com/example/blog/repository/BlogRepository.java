package com.example.blog.repository;

import com.example.blog.dto.projection.BlogPublic;
import com.example.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<BlogPublic> findByStatusOrderByPublishedAtDesc(Pageable pageable, Boolean status);

    List<Blog> findByTitleContainsIgnoreCaseAndStatusOrderByPublishedAtDesc(String title, Boolean status);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    List<Blog> findByCategories_NameAndStatusOrderByPublishedAtDesc(String name, Boolean status);


}