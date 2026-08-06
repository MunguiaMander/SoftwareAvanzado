package com.mander.backend.config;

import com.mander.backend.model.User;
import com.mander.backend.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) {
            return;
        }
        create("admin", "admin123", "ADMIN");
        create("user", "user123", "USER");
    }

    private void create(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);
        users.save(user);
    }
}
