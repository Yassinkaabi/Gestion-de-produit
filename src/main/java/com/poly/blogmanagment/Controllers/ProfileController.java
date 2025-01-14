package com.poly.blogmanagment.Controllers;

import com.poly.blogmanagment.entities.Post;
import com.poly.blogmanagment.entities.Profile;
import com.poly.blogmanagment.security.UserPrincipal;
import com.poly.blogmanagment.service.PostService;
import com.poly.blogmanagment.service.ProfileService;
import com.poly.blogmanagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    private PostService postService;

    @GetMapping("/create")
    public String createProfilePage(Model model) {
        model.addAttribute("profile", new Profile()); // Ajout d'un profil vide pour la création
        return "user/create-profile"; // Retourne le template Thymeleaf "create-create-profile.html"
    }


    @GetMapping("/my-profile")
    public String getMyProfile(@AuthenticationPrincipal UserPrincipal currentUser, Model model) {
        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non connecté");
        }

        // Récupérer le profil de l'utilisateur
        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        if (profile == null) {
            profile = new Profile(); // Créer un profil vide si aucun n'existe
        }

        // Récupérer les posts créés par l'utilisateur
        List<Post> postsByUser = postService.getPostsByUser(currentUser.getId());

        // Ajouter les données au modèle
        model.addAttribute("user", currentUser); // Injecte l'utilisateur connecté
        model.addAttribute("profile", profile); // Injecte le profil de l'utilisateur
        model.addAttribute("posts", postsByUser); // Injecte les posts de l'utilisateur

        return "user/my-profile"; // Renvoie la vue Thymeleaf
    }

    // Ajouter ou mettre à jour le profil pour l'utilisateur connecté
    @PostMapping
    public String saveOrUpdateProfile(@ModelAttribute Profile profile, @AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non connecté");
        }

        // Associer l'ID de l'utilisateur connecté au profil avant de le sauvegarder
        profileService.saveOrUpdateProfile(profile, currentUser.getId());

        return "redirect:/profile/my-profile"; // Redirection vers la page de création du profil ou une page de succès
    }
}
