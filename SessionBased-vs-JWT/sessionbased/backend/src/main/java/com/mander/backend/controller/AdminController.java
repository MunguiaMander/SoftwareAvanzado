package com.mander.backend.controller;

import com.mander.backend.dto.Dtos;
import com.mander.backend.model.User;
import com.mander.backend.repository.UserRepository;
import com.mander.backend.service.SessionService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository users;
    private final SessionService sessions;

    public AdminController(UserRepository users, SessionService sessions) {
        this.users = users;
        this.sessions = sessions;
    }

    @GetMapping("/users")
    public ResponseEntity<?> list() {
        List<Dtos.AdminUserView> result = users.findAll().stream()
                .map(u -> new Dtos.AdminUserView(u.getId(), u.getUsername(), u.getRole(), u.isEnabled(),
                        sessions.countActive(u.getUsername())))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/{username}/block")
    public ResponseEntity<?> block(@PathVariable String username) {
        User user = users.findByUsername(username).orElseThrow();
        user.setEnabled(false);
        users.save(user);
        sessions.invalidateAll(username);
        return ResponseEntity.ok(new Dtos.MessageResponse("Usuario bloqueado y sus sesiones cerradas"));
    }

    @PostMapping("/users/{username}/unblock")
    public ResponseEntity<?> unblock(@PathVariable String username) {
        User user = users.findByUsername(username).orElseThrow();
        user.setEnabled(true);
        users.save(user);
        return ResponseEntity.ok(new Dtos.MessageResponse("Usuario desbloqueado"));
    }
}
