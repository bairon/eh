package com.eldritch.security;

import com.eldritch.user.UserRepository;
import com.eldritch.user.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nickname = authentication.getName();
        String password = authentication.getCredentials().toString();

        String email = nickname + "@eldritch.com";
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new BadCredentialsException("User not found");
        }

        if ((user.get().getPassword() == null || user.get().getPassword().isEmpty())
                && (password == null || password.isEmpty())) {
            // Allow login without password for users who registered via OAuth
        } else if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // Return a CustomAuthentication object with the nickname and no authorities
        return new CustomAuthentication(nickname, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}