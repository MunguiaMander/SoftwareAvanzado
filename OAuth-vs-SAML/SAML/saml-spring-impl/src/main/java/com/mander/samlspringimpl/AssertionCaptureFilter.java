package com.mander.samlspringimpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AssertionCaptureFilter extends OncePerRequestFilter {

    public static final String SESSION_KEY = "RAW_SAML_RESPONSE";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String samlResponse = request.getParameter("SAMLResponse");
        if (samlResponse != null && !samlResponse.isBlank()) {
            try {
                String xml = new String(
                        Base64.getDecoder().decode(samlResponse), StandardCharsets.UTF_8);
                request.getSession(true).setAttribute(SESSION_KEY, xml);
            } catch (IllegalArgumentException ex) {
                logger.warn("SAMLResponse no es base64 valido", ex);
            }
        }

        chain.doFilter(request, response);
    }
}
