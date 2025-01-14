package com.poly.blogmanagment.service;


import com.poly.blogmanagment.Request.PostRequest;
import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.security.UserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Page<Post> getAllPosts(int page, int size);

    //List<Post> getPostsByCategory(Long id, int page, int size);
    List<Post> getPostsByUser(Long userId);
    Post updatePost(Long id, Post newPostRequest, UserPrincipal currentUser);

    void deletePost(Long id, UserPrincipal currentUser);

    Post addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPost(Long id);
}