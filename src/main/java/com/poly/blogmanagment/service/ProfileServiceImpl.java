package com.poly.blogmanagment.service;

import com.poly.blogmanagment.entities.Profile;
import com.poly.blogmanagment.entities.user.UserModel;
import com.poly.blogmanagment.repository.ProfileRepository;
import com.poly.blogmanagment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;
    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElse(null);
    }

    @Override
    public Profile saveOrUpdateProfile(Profile profile, Long userId) {
        Profile existingProfile = profileRepository.findByUserId(userId).orElse(null);

        if (existingProfile != null) {
            existingProfile.setBio(profile.getBio());
            existingProfile.setPhoneNumber(profile.getPhoneNumber());
            return profileRepository.save(existingProfile);
        } else {
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            profile.setUser(user);
            return profileRepository.save(profile);
        }
    }

    @Override
    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));
    }
}
