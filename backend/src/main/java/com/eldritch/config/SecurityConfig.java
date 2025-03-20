package com.eldritch.config;

import com.eldritch.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession() // Ensure session fixation protection
                        .maximumSessions(1) // Allow only one session per user
                        .expiredUrl("/login?expired") // Redirect to login page if session expires
                )
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/assets/**", "/favicon.ico").permitAll() // Allow access to static resources
                        .requestMatchers("/eldritch-websocket", "/eldritch-websocket/**").permitAll() // Allow WebSocket connections
                        .requestMatchers("/api/auth/**").permitAll() // Allow custom login
                        .requestMatchers("/api/user").permitAll() // Allow custom login
                        .requestMatchers("/oauth2/authorization/google").permitAll() // Allow Google OAuth2 login
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/") // Redirect to the home page if login is required
                        .defaultSuccessUrl("/", true) // Redirect to the home page after successful login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redirect to the home page after logout
                        .invalidateHttpSession(true) // Invalidate the session
                        .deleteCookies("JSESSIONID") // Delete the session cookie
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

}