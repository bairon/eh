package com.eldritch.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id); // Add this line
    Optional<User> findByLogin(String login); // Add this line
    Optional<User> findByEmail(String email);
}