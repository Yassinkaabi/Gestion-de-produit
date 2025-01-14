package com.poly.blogmanagment.service;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import com.poly.blogmanagment.security.UserPrincipal;

import java.util.List;

public interface TagService {
    Tag getTagByName(String name);
    List<Tag> getAllTags();
    Tag addTag(Tag tag, UserPrincipal currentUser);
    List<Post> getPostsByTagName(String tagName);
    List<Post> getPostsByTags(List<String> tagNames);
}