/*
 * Proyecto: GreenHouse Manager
 * Archivo: UsuarioController.java
 * Descripcion: Controlador REST para usuarios.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.api.controller;

import com.greenhouse.manager.api.dto.request.UsuarioRequest;
import com.greenhouse.manager.api.dto.response.UsuarioResponse;
import com.greenhouse.manager.config.OpenApiSecurity;
import com.greenhouse.manager.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios")
@PreAuthorize("hasRole('ADMIN')")
@OpenApiSecurity
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Creates a new UsuarioController.
     *
     * @param usuarioService user service
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lists all users.
     *
     * @return list of users
     */
    @Operation(summary = "List users")
    @ApiResponse(responseCode = "200", description = "Ok")
    @GetMapping
    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    /**
     * Retrieves a user by id.
     *
     * @param id user id
     * @return user
     */
    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public UsuarioResponse getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    /**
     * Creates a new user.
     *
     * @param request user request
     * @return created user
     */
    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201", description = "Created")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UsuarioResponse createUsuario(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.createUsuario(request);
    }

    /**
     * Updates a user.
     *
     * @param id user id
     * @param request user request
     * @return updated user
     */
    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PutMapping("/{id}")
    public UsuarioResponse updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.updateUsuario(id, request);
    }

    /**
     * Deletes a user.
     *
     * @param id user id
     */
    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }
}
