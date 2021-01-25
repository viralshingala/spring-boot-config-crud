package com.vshingala.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vshingala.spring.datajpa.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
	List<Blog> findByPublished(boolean published);
	List<Blog> findByTitleContaining(String title);
}
