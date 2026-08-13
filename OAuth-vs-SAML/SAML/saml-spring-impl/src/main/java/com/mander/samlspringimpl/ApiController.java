package com.mander.samlspringimpl;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public/info")
    public Map<String, String> publicInfo() {
        return Map.of("message", "Contenido publico. No hizo falta ninguna assertion.");
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nameId", principal.getName());
        result.put("relyingParty", principal.getRelyingPartyRegistrationId());

        Map<String, List<Object>> attributes = new LinkedHashMap<>();
        principal.getAttributes().forEach(attributes::put);
        result.put("attributes", attributes);

        return result;
    }

    @GetMapping("/debug/assertion")
    public Map<String, Object> assertion(HttpSession session) {
        Object raw = session.getAttribute(AssertionCaptureFilter.SESSION_KEY);

        if (raw == null) {
            return Map.of(
                    "available", false,
                    "message", "No hay assertion capturada. Cierra sesion y vuelve a entrar.");
        }

        String xml = raw.toString();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("available", true);
        result.put("xml", xml);
        result.put("sizeBytes", xml.getBytes(StandardCharsets.UTF_8).length);
        result.put("lineCount", xml.split("\r\n|\r|\n").length);
        result.put("hasSignature", xml.contains("Signature"));
        result.put("hasAudienceRestriction", xml.contains("AudienceRestriction"));
        result.put("hasConditions", xml.contains("Conditions"));
        result.put("hasAttributeStatement", xml.contains("AttributeStatement"));
        return result;
    }
}
