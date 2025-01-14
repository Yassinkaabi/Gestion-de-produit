package com.poly.blogmanagment.service;

import com.poly.blogmanagment.entities.Profile;
import com.poly.blogmanagment.security.UserPrincipal;

public interface ProfileService {
    Profile saveOrUpdateProfile(Profile profile, Long userId);
    Profile getProfileById(Long id);
    Profile getProfileByUserId(Long userId); // Add this method

}
