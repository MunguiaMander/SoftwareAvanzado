package com.mander.backend.controller;

import com.mander.backend.dto.Dtos;
import com.mander.backend.model.User;
import com.mander.backend.repository.UserRepository;
import com.mander.backend.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessions;

    public AuthController(AuthenticationManager authenticationManager, UserRepository users,
            PasswordEncoder passwordEncoder, SessionService sessions) {
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.sessions = sessions;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Dtos.LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new Dtos.MessageResponse("Credenciales invalidas"));
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        sessions.register(request.username(), session);

        User user = users.findByUsername(request.username()).orElseThrow();
        return ResponseEntity.ok(new Dtos.UserInfo(user.getUsername(), user.getRole()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new Dtos.MessageResponse("Sesion cerrada"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        User user = users.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(new Dtos.UserInfo(user.getUsername(), user.getRole()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody Dtos.ChangePasswordRequest request,
            Authentication authentication, HttpServletRequest httpRequest) {
        User user = users.findByUsername(authentication.getName()).orElseThrow();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body(new Dtos.MessageResponse("Password actual incorrecto"));
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        users.save(user);

        String currentSessionId = httpRequest.getSession(false).getId();
        sessions.invalidateAllExcept(user.getUsername(), currentSessionId);

        return ResponseEntity.ok(new Dtos.MessageResponse("Password actualizado, las demas sesiones fueron cerradas"));
    }
}
