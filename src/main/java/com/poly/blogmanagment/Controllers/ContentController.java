package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Tag;
import com.poly.blogmanagment.repository.PostRepository;
import com.poly.blogmanagment.service.PostService;
import com.poly.blogmanagment.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class ContentController {

    private final PostService postService;

    private TagService tagService;

    @GetMapping("/req/login")
    public String login(){
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(){
        return "index";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/")
    public String listPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            //@RequestParam List<String> tags,
                            Model model) {
        //List<Post> postsByTags = tagService.getPostsByTags(tags);
        Page<Post> PageHome = postService.getAllPosts(page, size);
        //model.addAttribute("postsByTags", postsByTags);
        model.addAttribute("posts", PageHome.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", PageHome.getTotalPages());
        model.addAttribute("pageSize", size);
        return "index";
    }

    /*@GetMapping("/postsByTags")
    public String getPostsByTags(@RequestParam List<String> tags, Model model) {
        List<Post> posts = tagService.getPostsByTags(tags);
        model.addAttribute("posts", posts);
        model.addAttribute("tagName", tags);
        return "tag/posts-by-tag";
    }*/

}
