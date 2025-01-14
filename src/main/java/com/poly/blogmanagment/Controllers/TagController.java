package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import com.poly.blogmanagment.security.UserPrincipal;
import com.poly.blogmanagment.service.PostService;
import com.poly.blogmanagment.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    /*@GetMapping
    public String getAllTags(Model model) {
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "tag/tags";
    }*/

    @GetMapping
    public String listPosts(Model model) {
        List<Tag> allTags = tagService.getAllTags(); // Récupère tous les tags
        model.addAttribute("allTags", allTags); // Ajoute les tags au modèle
        return "tag/tags"; // Vue correspondant à votre HTML
    }

    @GetMapping("/postsByTags")
    public String getPostsByTags(@RequestParam List<String> tags, Model model) {
        List<Post> posts = tagService.getPostsByTags(tags);
        model.addAttribute("posts", posts); // Posts filtrés
        model.addAttribute("tagName", tags); // Posts filtrés
        model.addAttribute("allTags", tagService.getAllTags()); // Recharger tous les tags
        return "tag/tags";
    }

    // Afficher le formulaire de création de tag
    @GetMapping("/create")
    public String showCreateTagForm() {
        return "tag/create-tag";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public String addTag(@Valid Tag tag, UserPrincipal currentUser, Model model) {
        Tag newTag = tagService.addTag(tag, currentUser);
        model.addAttribute("tag", newTag);
        return "redirect:/tags";
    }

    @GetMapping("/{name}")
    public String getTag(@PathVariable("name") String name, Model model) {
        Tag tag = tagService.getTagByName(name);
        model.addAttribute("tag", tag);
        return "tag-details"; // The name of the Thymeleaf template to render (tag-details.html)
    }

    @GetMapping("/{name}/posts")
    public String getPostsByTag(@PathVariable("name") String tagName, Model model) {
        List<Post> posts = tagService.getPostsByTagName(tagName); // Fetch posts by tag name
        model.addAttribute("posts", posts);
        model.addAttribute("tagName", tagName);
        return "tag/posts-by-tag"; // Template to render posts
    }

}
