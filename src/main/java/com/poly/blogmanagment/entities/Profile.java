package com.poly.blogmanagment.entities;

import com.poly.blogmanagment.entities.user.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La bio ne peut pas être vide")
    private String bio;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]+$", message = "Le numéro de téléphone doit contenir uniquement des chiffres")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user; // Référence à l'utilisateur
}
