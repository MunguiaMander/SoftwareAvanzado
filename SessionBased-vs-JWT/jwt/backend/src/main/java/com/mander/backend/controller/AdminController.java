package com.mander.backend.controller;

import com.mander.backend.dto.Dtos;
import com.mander.backend.model.User;
import com.mander.backend.repository.UserRepository;

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

    public AdminController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/users")
    public ResponseEntity<?> list() {
        List<Dtos.AdminUserView> result = users.findAll().stream()
                .map(u -> new Dtos.AdminUserView(u.getId(), u.getUsername(), u.getRole(), u.isEnabled()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/{username}/block")
    public ResponseEntity<?> block(@PathVariable String username) {
        User user = users.findByUsername(username).orElseThrow();
        user.setEnabled(false);
        users.save(user);
        return ResponseEntity.ok(new Dtos.MessageResponse(
                "Usuario bloqueado. No podra iniciar sesion, pero su token actual sigue funcionando hasta que expire."));
    }

    @PostMapping("/users/{username}/unblock")
    public ResponseEntity<?> unblock(@PathVariable String username) {
        User user = users.findByUsername(username).orElseThrow();
        user.setEnabled(true);
        users.save(user);
        return ResponseEntity.ok(new Dtos.MessageResponse("Usuario desbloqueado"));
    }
}
