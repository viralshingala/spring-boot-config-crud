package com.vshingala.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vshingala.spring.datajpa.model.Blog;
import com.vshingala.spring.datajpa.repository.BlogRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BlogController {

	@Autowired
	BlogRepository blogRepository;

	@GetMapping("/blogs")
	public ResponseEntity<List<Blog>> getAllblogs(@RequestParam(required = false) String title) {
		try {
			List<Blog> blogs = new ArrayList<Blog>();

			if (title == null)
				blogRepository.findAll().forEach(blogs::add);
			else
				blogRepository.findByTitleContaining(title).forEach(blogs::add);

			if (blogs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(blogs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/blogs/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable("id") long id) {
		Optional<Blog> blogData = blogRepository.findById(id);

		if (blogData.isPresent()) {
			return new ResponseEntity<>(blogData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/blogs")
	public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
		try {
			Blog _blog = blogRepository
					.save(new Blog(blog.getTitle(), blog.getDescription(), false));
			return new ResponseEntity<>(_blog, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/blogs/{id}")
	public ResponseEntity<Blog> updateBlog(@PathVariable("id") long id, @RequestBody Blog blog) {
		Optional<Blog> blogData = blogRepository.findById(id);

		if (blogData.isPresent()) {
			Blog _blog = blogData.get();
			_blog.setTitle(blog.getTitle());
			_blog.setDescription(blog.getDescription());
			_blog.setPublished(blog.isPublished());
			return new ResponseEntity<>(blogRepository.save(_blog), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/blogs/{id}")
	public ResponseEntity<HttpStatus> deleteBlog(@PathVariable("id") long id) {
		try {
			blogRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/blogs")
	public ResponseEntity<HttpStatus> deleteAllblogs() {
		try {
			blogRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/blogs/published")
	public ResponseEntity<List<Blog>> findByPublished() {
		try {
			List<Blog> blogs = blogRepository.findByPublished(true);

			if (blogs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(blogs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
