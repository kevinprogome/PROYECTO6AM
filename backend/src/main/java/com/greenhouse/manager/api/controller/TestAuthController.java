/*
 * Proyecto: GreenHouse Manager
 * Archivo: TestAuthController.java
 * Descripcion: Endpoint de autenticacion de pruebas para generar JWT.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-06
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.domain.entity.Usuario;
import com.greenhouse.manager.domain.enums.AuthProvider;
import com.greenhouse.manager.domain.enums.UserRole;
import com.greenhouse.manager.domain.repository.UsuarioRepository;
import com.greenhouse.manager.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Locale;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that generates test JWT tokens for automation environments.
 */
@RestController
@RequestMapping("/api/auth")
@Profile({"dev", "test"})
@Tag(name = "Auth", description = "Autenticacion para pruebas")
public class TestAuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    /**
     * Creates a new TestAuthController.
     *
     * @param usuarioRepository user repository
     * @param jwtService jwt service
     */
    public TestAuthController(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    /**
     * Generates a JWT token for automated tests.
     *
     * @param request test auth request
     * @return test auth response
     */
    @Operation(summary = "Generate test JWT token")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/test-token")
    public TestAuthResponse generateTestToken(@Valid @RequestBody TestAuthRequest request) {
        UserRole role = resolveRole(request.getRole());
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .map(existing -> updateRole(existing, role))
            .orElseGet(() -> createTestUser(request.getEmail(), role));
        String token = jwtService.generateToken(usuario);
        return new TestAuthResponse(token, usuario.getId(), usuario.getEmail(), usuario.getRol().name());
    }

    private Usuario createTestUser(String email, UserRole role) {
        Usuario usuario = new Usuario();
        usuario.setNombre(email.split("@")[0]);
        usuario.setEmail(email);
        usuario.setRol(role);
        usuario.setProvider(AuthProvider.GOOGLE);
        usuario.setProviderId(email);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    private Usuario updateRole(Usuario usuario, UserRole role) {
        usuario.setRol(role);
        return usuarioRepository.save(usuario);
    }

    private UserRole resolveRole(String role) {
        if (role == null || role.isBlank()) {
            return UserRole.OPERADOR;
        }
        return UserRole.valueOf(role.toUpperCase(Locale.ROOT));
    }

    /**
     * Request payload for test auth.
     */
    public static class TestAuthRequest {

        @NotBlank
        @Email
        private String email;

        @Size(max = 20)
        private String role;

        /**
         * Gets the email.
         *
         * @return email
         */
        public String getEmail() {
            return email;
        }

        /**
         * Sets the email.
         *
         * @param email user email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * Gets the role.
         *
         * @return role
         */
        public String getRole() {
            return role;
        }

        /**
         * Sets the role.
         *
         * @param role user role
         */
        public void setRole(String role) {
            this.role = role;
        }
    }

    /**
     * Response payload for test auth.
     */
    public static class TestAuthResponse {

        private final String token;
        private final Long userId;
        private final String email;
        private final String role;

        /**
         * Creates a new test auth response.
         *
         * @param token jwt token
         * @param userId user id
         * @param email user email
         * @param role user role
         */
        public TestAuthResponse(String token, Long userId, String email, String role) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.role = role;
        }

        /**
         * Gets the token.
         *
         * @return token
         */
        public String getToken() {
            return token;
        }

        /**
         * Gets the user id.
         *
         * @return user id
         */
        public Long getUserId() {
            return userId;
        }

        /**
         * Gets the email.
         *
         * @return email
         */
        public String getEmail() {
            return email;
        }

        /**
         * Gets the role.
         *
         * @return role
         */
        public String getRole() {
            return role;
        }
    }
}
