package com.poly.blogmanagment.service;

import com.poly.blogmanagment.Request.PostRequest;
import com.poly.blogmanagment.entities.*;
import com.poly.blogmanagment.entities.user.UserModel;
import com.poly.blogmanagment.repository.CategoryRepository;
import com.poly.blogmanagment.repository.PostRepository;
import com.poly.blogmanagment.repository.TagRepository;
import com.poly.blogmanagment.repository.UserRepository;
import com.poly.blogmanagment.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final String CREATED_AT = "createdAt";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    /*@Override
    public List<Post> getPostsByCategory(Long id, int page, int size) {
        validatePageNumberAndSize(page, size);

        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Category not found with ID: " + id));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, CREATED_AT));
        Page<Post> posts = postRepository.findByCategory(category.getId(), pageable);
        return posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();
    }*/

    @Override
    public Page<Post> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllPostWithTags(pageable);
    }

    @Override
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findAllByUserId(userId);
    }


    @Override
    public Post updatePost(Long id, Post newPostRequest, UserPrincipal currentUser) {
        if (id == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        if (!post.getUser().getId().equals(currentUser.getUsername()) &&
                !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new RuntimeException("You don't have permission to update this post.");
        }

        Category category = categoryRepository.findById(newPostRequest.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + newPostRequest.getCategory().getId()));

        post.setTitle(newPostRequest.getTitle());
        post.setBody(newPostRequest.getBody());
        post.setCategory(category);

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        if (!post.getUser().getId().equals(currentUser.getUsername()) &&
                !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new RuntimeException("You don't have permission to delete this post.");
        }

        postRepository.delete(post);
    }


    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Post addPost(PostRequest postRequest, UserPrincipal currentUser) {
        // Retrieve the user by the username
        UserModel user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + currentUser.getUsername()));

        // Retrieve the category
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + postRequest.getCategoryId()));

        // Create a new Post
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setBody(postRequest.getBody());
        post.setCategory(category);
        post.setUser(user);

        List<Tag> tags = tagRepository.findAllById(postRequest.getTagIds());
        if (tags.isEmpty()) {
            throw new IllegalArgumentException("No tags found for the provided IDs");
        }
        post.setTags(tags);


        // Handle image upload
        if (postRequest.getImage() != null && !postRequest.getImage().isEmpty()) {
            try {
                String imagePath = fileStorageService.storeFile(postRequest.getImage());
                post.setImageUrl(imagePath); // Save the image path
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        // Save the post
        return postRepository.save(post);
    }


    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be less than zero.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
    }
}
