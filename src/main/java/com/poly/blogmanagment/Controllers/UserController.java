package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.user.UserModel;
import com.poly.blogmanagment.service.PostService;
import com.poly.blogmanagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<UserModel> user = userService.findUserById(id);
        List<Post> postUser = postService.getPostsByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("postUser", postUser);
        return "user/my-profile";
    }

}
