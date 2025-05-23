package com.eldritch.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private Map<String, UserData> userCache = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String nickname, String login, String password) { // Update this method
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Check if login already exists
        if (userRepository.findByLogin(login).isPresent()) { // Add this check
            throw new RuntimeException("Login already exists");
        }

        // Create and save the new user
        User user = new User();
        user.setEmail(email);
        user.setNickname(nickname);
        user.setLogin(login); // Add this line
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        userCache.put(saved.getId(), new UserData(saved));
        return saved;
    }

    public UserData getUserData(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userEntity = user.get();
            return new UserData(userEntity.getId(), userEntity.getLogin(), userEntity.getEmail(), userEntity.getNickname(), userEntity.getLanguage());
        }
        return null;
    }
    public UserData getUserDataById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userEntity = user.get();
            return new UserData(userEntity.getId(), userEntity.getLogin(), userEntity.getEmail(), userEntity.getNickname(), userEntity.getLanguage());
        }
        return null;
    }

    public String getNickname(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getNickname).orElse(null);
    }

    public void setNickname(String email, String nickname, String password) throws Exception {
        String sanitizedNickname = nickname.replace("\"", "").trim();
        Optional<User> existingUser = userRepository.findByEmail(sanitizedNickname);

        if (existingUser.isPresent() && !existingUser.get().getEmail().equals(email)) {
            throw new Exception("Nickname is already taken.");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            user = Optional.of(new User());
            user.get().setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            user.get().setPassword(passwordEncoder.encode(password));
        }

        user.get().setNickname(sanitizedNickname);
        userRepository.save(user.get());
    }

    public boolean verifyPassword(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.get().getPassword());
    }

    public void updatePreferences(String id, String nickname, String language) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userEntity = user.get();
            if (nickname != null && !nickname.isEmpty()) {
                userEntity.setNickname(nickname);
            }
            if (language != null && !language.isEmpty()) {
                userEntity.setLanguage(language);
            }
            userRepository.save(userEntity);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}