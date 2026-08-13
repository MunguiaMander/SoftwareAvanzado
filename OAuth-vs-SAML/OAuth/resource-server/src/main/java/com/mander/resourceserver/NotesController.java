package com.mander.resourceserver;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:4300")
public class NotesController {

    public record Note(long id, String owner, String text) {}
    public record NewNote(String text) {}

    private final Map<Long, Note> notes = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public NotesController() {
        seed("demo", "OAuth 2.0 delega autorizacion, no autenticacion.");
        seed("demo", "El access token solo sirve para lo que dicen sus scopes.");
    }

    private void seed(String owner, String text) {
        long id = sequence.incrementAndGet();
        notes.put(id, new Note(id, owner, text));
    }

    @GetMapping
    public List<Note> list(@AuthenticationPrincipal Jwt jwt) {
        String owner = jwt.getSubject();
        return notes.values().stream()
                .filter(n -> n.owner().equals(owner) || n.owner().equals("demo"))
                .sorted(Comparator.comparingLong(Note::id))
                .toList();
    }

    @PostMapping
    public Note create(@AuthenticationPrincipal Jwt jwt, @RequestBody NewNote body) {
        long id = sequence.incrementAndGet();
        Note note = new Note(id, jwt.getSubject(), body.text());
        notes.put(id, note);
        return note;
    }

    @GetMapping("/whoami")
    public Map<String, Object> whoami(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "sub", jwt.getSubject(),
                "email", Optional.ofNullable(jwt.getClaimAsString("email")).orElse("(no expuesto)"),
                "scopes", Optional.ofNullable(jwt.getClaimAsString("scope")).orElse(""),
                "issuer", String.valueOf(jwt.getIssuer()),
                "expiresAt", String.valueOf(jwt.getExpiresAt()));
    }
}
