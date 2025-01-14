package com.poly.blogmanagment.repository;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.name IN :tagNames")
    List<Post> findPostsByTagNames(@Param("tagNames") List<String> tagNames);

    Tag findByName(String name);
    List<Tag> findAllById(Iterable<Long> ids);

}
