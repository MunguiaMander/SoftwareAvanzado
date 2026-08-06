package com.mander.backend.controller;

import com.mander.backend.dto.Dtos;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

    @GetMapping("/api/public/info")
    public ResponseEntity<?> publicInfo() {
        return ResponseEntity.ok(new Dtos.MessageResponse("Contenido publico, no requiere autenticacion"));
    }

    @GetMapping("/api/protected/data")
    public ResponseEntity<?> protectedData(Authentication authentication) {
        String message = "Contenido protegido para " + authentication.getName()
                + ". Validado con la firma del token, sin consultar la base de datos.";
        return ResponseEntity.ok(new Dtos.MessageResponse(message));
    }
}
