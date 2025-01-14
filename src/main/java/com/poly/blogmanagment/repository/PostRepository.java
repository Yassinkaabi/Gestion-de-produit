package com.poly.blogmanagment.repository;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.tags")
    Page<Post> findAllPostWithTags(Pageable pageable);
    List<Post> findAllByTagsContaining(Tag tag);
    List<Post> findAllByUserId(Long userId);
    //Page<Post> findByCreatedBy(Long userId, Pageable pageable);

    //Page<Post> findByCategory(Long categoryId, Pageable pageable);


    //Page<Post> findByTags(List<Tag> tags, Pageable pageable);

    //Long countByCreatedBy(Long userId);
}