package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.Request.PostRequest;
import com.poly.blogmanagment.entities.Category;
import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import com.poly.blogmanagment.security.UserPrincipal;
import com.poly.blogmanagment.service.CategoryService;
import com.poly.blogmanagment.service.PostService;
import com.poly.blogmanagment.service.TagService;
import com.poly.blogmanagment.service.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping
    public String listPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            Model model) {
        Page<Post> posts = postService.getAllPosts(page, size);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("pageSize", size);
        return "posts/list";
    }


    // Récupérer un post par ID
    @GetMapping("/consult/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }

    // Formulaire pour ajouter un nouveau post
    @GetMapping("/new")
    public String showAddPostForm(
            Tag tag,
            Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        model.addAttribute("postRequest", new PostRequest());
        return "posts/create";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public String addPost(
            @ModelAttribute("postRequest") PostRequest postRequest,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        if (tagIds == null || tagIds.isEmpty()) {
            throw new IllegalArgumentException("Tag IDs must not be null or empty");
        }

        postRequest.setTagIds(tagIds); // Assurez-vous que PostRequest inclut un champ `tagIds`.
        System.out.println("Received tag IDs: " + tagIds);
        postService.addPost(postRequest, currentUser);
        return "redirect:/posts";
    }


    // Formulaire pour mettre à jour un post existant
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("post", post);
        model.addAttribute("categories", categories);
        return "posts/edit"; // Render the edit page
    }


    // Mettre à jour un post existant
    @PostMapping("/update/{id}")
    public String updatePost(
            @PathVariable Long id,
            @ModelAttribute Post postRequest,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        postService.updatePost(id, postRequest, currentUser);
        return "redirect:/posts"; // Redirect after updating
    }

    // Supprimer un post
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(id, currentUser);
        return "redirect:/posts";
    }

    /*@GetMapping("/searchByTag")
    public String searchByTag(@RequestParam("tag") String tagName, Model model) {
        Tag tag = tagService.getTagByName(tagName);
        if (tag != null) {
            model.addAttribute("posts", tag.getPosts());
        } else {
            model.addAttribute("posts", List.of());
        }
        return "posts/list"; // Retourne la même vue que pour la liste des posts
    }*/

    @GetMapping("/tags")
    public String getPostsByTags(@RequestParam List<String> tags, Model model) {
        List<Post> posts = tagService.getPostsByTags(tags);
        model.addAttribute("posts", posts);
        model.addAttribute("tagName", tags);
        return "tag/posts-by-tag";
    }

}
