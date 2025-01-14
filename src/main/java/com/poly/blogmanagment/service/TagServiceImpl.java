package com.poly.blogmanagment.service;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import com.poly.blogmanagment.repository.PostRepository;
import com.poly.blogmanagment.repository.TagRepository;
import com.poly.blogmanagment.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Post> getPostsByTagName(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        return postRepository.findAllByTagsContaining(tag);
    }

    @Override
    public Tag addTag(Tag tag, UserPrincipal currentUser) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Post> getPostsByTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            throw new IllegalArgumentException("Tag names cannot be null or empty");
        }
        return tagRepository.findPostsByTagNames(tagNames);
    }

}
