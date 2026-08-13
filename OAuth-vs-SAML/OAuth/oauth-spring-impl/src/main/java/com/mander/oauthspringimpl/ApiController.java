package com.mander.oauthspringimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RestClient restClient = RestClient.create();
    private static final String RESOURCE_SERVER = "http://localhost:8082";

    @GetMapping("/public/info")
    public Map<String, String> publicInfo() {
        return Map.of("message", "Contenido publico. No hizo falta ningun token.");
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OidcUser user) {
        return Map.of(
                "sub", user.getSubject(),
                "name", Optional.ofNullable(user.getFullName()).orElse(""),
                "email", Optional.ofNullable(user.getEmail()).orElse(""),
                "picture", Optional.ofNullable(user.getPicture()).orElse(""));
    }

    @GetMapping("/debug/tokens")
    public Map<String, Object> tokens(
            @AuthenticationPrincipal OidcUser user,
            @RegisteredOAuth2AuthorizedClient("bff-client") OAuth2AuthorizedClient client) {

        String rawIdToken = user.getIdToken().getTokenValue();
        var accessToken = client.getAccessToken();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("idTokenRaw", rawIdToken);
        result.put("idTokenSizeBytes", rawIdToken.getBytes(StandardCharsets.UTF_8).length);
        result.put("idTokenHeader", decodeSegment(rawIdToken, 0));
        result.put("idTokenPayload", decodeSegment(rawIdToken, 1));
        result.put("accessTokenRaw", accessToken.getTokenValue());
        result.put("accessTokenPayload", decodeSegment(accessToken.getTokenValue(), 1));
        result.put("grantedScopes", accessToken.getScopes());
        result.put("expiresAt", String.valueOf(accessToken.getExpiresAt()));
        result.put("hasRefreshToken", client.getRefreshToken() != null);
        return result;
    }

    private String decodeSegment(String jwt, int index) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                return "(no es un JWT: token opaco)";
            }
            return new String(Base64.getUrlDecoder().decode(parts[index]), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return "(no se pudo decodificar: " + ex.getMessage() + ")";
        }
    }

    @GetMapping("/notes")
    public ResponseEntity<?> listNotes(
            @RegisteredOAuth2AuthorizedClient("bff-client") OAuth2AuthorizedClient client) {
        return callResourceServer(client, "GET", "/api/notes", null);
    }

    @PostMapping("/notes")
    public ResponseEntity<?> createNote(
            @RegisteredOAuth2AuthorizedClient("bff-client") OAuth2AuthorizedClient client,
            @RequestBody Map<String, String> body) {
        return callResourceServer(client, "POST", "/api/notes", body);
    }

    private ResponseEntity<?> callResourceServer(
            OAuth2AuthorizedClient client, String method, String path, Object body) {

        String token = client.getAccessToken().getTokenValue();
        try {
            if ("POST".equals(method)) {
                Object response = restClient.post()
                        .uri(RESOURCE_SERVER + path)
                        .header("Authorization", "Bearer " + token)
                        .body(body)
                        .retrieve()
                        .body(Object.class);
                return ResponseEntity.ok(response);
            }
            Object response = restClient.get()
                    .uri(RESOURCE_SERVER + path)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(Object.class);
            return ResponseEntity.ok(response);

        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "error", ex.getStatusCode().value() == 403
                            ? "403 Forbidden: el access token NO incluye el scope necesario"
                            : "Error del Resource Server",
                    "status", ex.getStatusCode().value(),
                    "grantedScopes", client.getAccessToken().getScopes(),
                    "detail", ex.getResponseBodyAsString()));
        }
    }
}
