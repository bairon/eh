package com.eldritch.controller;

import com.eldritch.model.User;
import com.eldritch.model.UserData;
import com.eldritch.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/preferences")
    public ResponseEntity<?> updatePreferences(@RequestBody Map<String, String> preferences, HttpSession session) {
        String id = (String) session.getAttribute("userId");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.updatePreferences(id, preferences.get("nickname"), preferences.get("language"));
        return ResponseEntity.ok(userService.getUserDataById(id));
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Object principal, HttpSession session) {
        if (principal instanceof OAuth2User oauth2User) {
            // OAuth2 user (e.g., Google login)
            String name = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email");
            UserData userData = userService.getUserData(email);
            if (userData == null) {
                User user = userService.registerUser(email, name, email, "");
                userData = new UserData(user.getId(), email, user.getEmail(), user.getNickname(), user.getLanguage());
            }
            return ResponseEntity.ok(userData);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String nickname = authentication.getName();
            UserData userData = userService.getUserData(nickname + "@eldritch.com");
            return ResponseEntity.ok(userData);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveUserData(@RequestBody Map<String, String> userData, HttpSession session) {
        String nickname = (String) session.getAttribute("nickname");
        if (nickname == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = nickname + "@eldritch.com";
        try {
            userService.setNickname(email, userData.get("nickname"), userData.get("password"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Inner class to represent user information
    public static class UserInfo {
        private String name;
        private String nickname;
        private boolean authenticated;

        public UserInfo(String name, String nickname, boolean authenticated) {
            this.name = name;
            this.nickname = nickname;
            this.authenticated = authenticated;
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean isAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }
    }
}