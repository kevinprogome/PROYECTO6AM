/*
 * Proyecto: GreenHouse Manager
 * Archivo: OAuth2AuthenticationSuccessHandler.java
 * Descripcion: Handler de exito para login OAuth2 con JWT.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenhouse.manager.api.exception.ApiErrorResponse;
import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.context.i18n.LocaleContextHolder; // ✅ CORRECTO
/**
 * Authentication success handler that returns a JWT token.
 */
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new OAuth2AuthenticationSuccessHandler.
     *
     * @param jwtService jwt service
     * @param usuarioRepository user repository
     * @param messageSource message source
     * @param objectMapper object mapper
     */
    public OAuth2AuthenticationSuccessHandler(
        JwtService jwtService,
        UsuarioRepository usuarioRepository,
        MessageSource messageSource,
        ObjectMapper objectMapper
    ) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    /**
     * Handles OAuth2 authentication success by issuing a JWT.
     *
     * @param request http request
     * @param response http response
     * @param authentication authentication object
     * @throws IOException on io errors
     * @throws ServletException on servlet errors
     */
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = (String) principal.getAttributes().get("email");

        if (email == null) {
            writeError(response, HttpStatus.BAD_REQUEST, "error.oauth2.email_no_encontrado", request.getRequestURI());
            return;
        }

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            writeError(response, HttpStatus.NOT_FOUND, "error.usuario.no_encontrado", request.getRequestURI());
            return;
        }

        String token = jwtService.generateToken(usuario);
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("tokenType", "Bearer");
        body.put("email", usuario.getEmail());
        body.put("rol", usuario.getRol().name());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        objectMapper.writeValue(response.getOutputStream(), body);
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String messageKey, String path)
        throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(messageKey, null, messageKey, locale);

        ApiErrorResponse error = new ApiErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.name());
        error.setMessage(message);
        error.setPath(path);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
