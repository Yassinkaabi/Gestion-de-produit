package com.poly.blogmanagment.security;

import com.poly.blogmanagment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AllArgsConstructor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService appUserService;

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> {
                    form.loginPage("/req/login").permitAll();
                    form.successHandler(customAuthenticationSuccessHandler());
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/req/signup","/","/api/users/{id}", "/posts/consult/{id}" , "/api/admin/signup","/posts/delete/{id}",
                            "/error","/css/**", "/js/**").permitAll();
                    auth.requestMatchers("/images/**", "/uploads/**").permitAll(); // Add the path where your images are stored
                    auth.requestMatchers( "/posts/edit/**").hasRole("USER");
                    auth.requestMatchers("/categories/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex ->
                        ex.accessDeniedPage("/error") // Gestion de la page en cas d'accès refusé
                )
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("");

            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/profile/my-profile");
            } else if (role.equals("ROLE_USER")) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect("/index");
            }
        };
    }

}

