package com.poly.blogmanagment.entities;

import com.poly.blogmanagment.entities.user.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    @Size(max = 65535, message = "Le contenu de l'article ne doit pas dépasser 65 535 caractères.")
    private String body;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToMany
    @JoinTable(
            name = "post_tags", // Nom de la table intermédiaire
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), // Clé étrangère vers Post
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id") // Clé étrangère vers Tag
    )
    private List<Tag> tags = new ArrayList<>();

}
