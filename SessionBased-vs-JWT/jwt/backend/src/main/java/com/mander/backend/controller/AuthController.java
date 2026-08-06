package com.mander.backend.controller;

import com.mander.backend.dto.Dtos;
import com.mander.backend.model.User;
import com.mander.backend.repository.UserRepository;
import com.mander.backend.security.JwtAuthFilter;
import com.mander.backend.service.JwtService;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository users,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Dtos.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (DisabledException e) {
            return ResponseEntity.status(401).body(new Dtos.MessageResponse("Usuario bloqueado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new Dtos.MessageResponse("Credenciales invalidas"));
        }

        User user = users.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generate(user.getUsername(), user.getRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCookie(token, jwtService.getAccessTokenTtl().getSeconds()))
                .body(new Dtos.UserInfo(user.getUsername(), user.getRole()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildCookie("", 0))
                .body(new Dtos.MessageResponse("Cookie eliminada. El token sigue siendo valido hasta que expire."));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        User user = users.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(new Dtos.UserInfo(user.getUsername(), user.getRole()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody Dtos.ChangePasswordRequest request,
            Authentication authentication) {
        User user = users.findByUsername(authentication.getName()).orElseThrow();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body(new Dtos.MessageResponse("Password actual incorrecto"));
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        users.save(user);

        return ResponseEntity.ok(new Dtos.MessageResponse(
                "Password actualizado. Los tokens emitidos antes siguen siendo validos hasta que expiren."));
    }

    private String buildCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(JwtAuthFilter.COOKIE_NAME, value)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build()
                .toString();
    }
}
