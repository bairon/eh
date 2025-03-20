package com.eldritch.controller;

import com.eldritch.model.User;
import com.eldritch.model.UserData;
import com.eldritch.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        session.removeAttribute("userId");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("success", "true");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody RegisterRequest request, HttpSession session) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()) // Update this line
            );

            // Set the authentication in the SecurityContextHolder and session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Fetch user data
            UserData user = userService.getUserData(request.getLogin() + "@eldritch.com"); // Update this line
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login failed: " + e.getMessage());
            response.put("success", "false");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request, HttpSession session) {
        try {
            String email = request.getLogin() + "@eldritch.com"; // Update this line
            String nickname = request.getLogin(); // Update this line
            User user = userService.registerUser(email, nickname, request.getLogin(), request.getPassword()); // Update this line

            // Authenticate the user after registration
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()) // Update this line
            );

            // Set the authentication in the SecurityContextHolder and session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            session.setAttribute("userId", user.getId());

            // Return user data
            return ResponseEntity.ok(new UserData(user.getId(), user.getLogin(), email, nickname, user.getLanguage()));
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("success", "false");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Inner classes for request bodies
    private static class RegisterRequest {
        private String nickname;
        private String login; // Add this line
        private String password;

        // Getters and setters
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLogin() { // Add this method
            return login;
        }

        public void setLogin(String login) { // Add this method
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}